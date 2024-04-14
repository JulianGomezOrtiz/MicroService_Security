package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Permission;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import com.ucaldas.mssecurity.Services.EncryptionService;
import com.ucaldas.mssecurity.Services.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ucaldas.mssecurity.Services.ValidatorsService;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/security")
public class SecurityController {

    @Autowired
    private ValidatorsService theValidatorsService;

    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private EncryptionService theEncryptionService;

    @Autowired
    private JwtService theJwtService;

    // Busca el usuario por medio del correo
    @PostMapping("login")
    public String login(@RequestBody User theUser, final HttpServletResponse response) throws IOException {
        String token = "";
        User actualUser = this.theUserRepository.getUserByEmail(theUser.getEmail());
        if (actualUser != null) {
            actualUser.getPassword().equals(this.theEncryptionService.convertSHA256(theUser.getPassword()));
            token = this.theJwtService.generateToken(actualUser);

        } else {
            response.sendError((HttpServletResponse.SC_UNAUTHORIZED));
        }
        return token;
    }

    /**
     * Validacion de permisos
     * 
     * @param Session theSession
     */

    @PostMapping("permisions-validation")
    public boolean permissionsValidation(final jakarta.servlet.http.HttpServletRequest request,
            @RequestBody Permission ThePermission) {
        boolean success = this.theValidatorsService.validationRolePermission(request, ThePermission.getUrl(),
                ThePermission.getMethod());

        return success;
    }

    @GetMapping("getSessionCode")
    public ResponseEntity<Integer> getSessionCode(@RequestParam String email) {
        User actualUser = theUserRepository.getUserByEmail(email);
        if (actualUser != null) {
            Optional<Session> userSessionOpt = sessionRepository.findByUserAndActive(actualUser, true);
            if (userSessionOpt.isPresent()) {
                return ResponseEntity.ok(userSessionOpt.get().getCode());
            }
        }
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(null);
    }

    @PostMapping("verifyCode2")
    public ResponseEntity<Boolean> verifyCode2(@RequestBody Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        String code = (String) requestBody.get("code");

        User actualUser = theUserRepository.getUserByEmail(email);
        if (actualUser == null) {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(false);
        }

        Optional<Session> userSessionOpt = sessionRepository.findByUserAndActive(actualUser, false);
        if (userSessionOpt.isPresent() && Integer.toString(userSessionOpt.get().getCode()).equals(code)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

/**
     * 2do factor de autenticación
     * 
     * @param Session theSession
     */
    
    @PutMapping("{id}/password")
    public ResponseEntity<Boolean> password(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        String newPassword = requestBody.get("password");

        User actualUser = theUserRepository.findById(id).orElse(null);
        if (actualUser != null) {
            actualUser.setPassword(theEncryptionService.convertSHA256(newPassword);
            theUserRepository.save(actualUser);
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(false);
        }
    }

    /**
     * cambio de contraseña
     * 
     * @param user theUser, HttpServletResponse response
     */

    @PostMapping("changePassword")
    public String changePassword(@RequestBody User theUser, final HttpServletResponse response) throws IOException {
        String validation = "";
        User actualUser = this.theUserRepository.getUserByEmail(theUser.getEmail());
        if (actualUser != null) {
            validation = actualUser.get_id();

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return validation;
    }

    /**
     * validación de token
     * 
     * @param request
     */

    @GetMapping("token-validation")
    public User tokenValidation(final HttpServletRequest request) {
        User theUser = this.theValidatorsService.getUser(request);
        return theUser;
    }

    /**
     * 2do factor de autenticación
     * 
     * @param user theUser, HttpServletResponse response
     */

    @PostMapping("2FA")
    public String second_factor(@RequestBody User theUser, final HttpServletResponse response) throws IOException {
        String token = "";
        User actualUser = this.theUserRepository.getUserByEmail(theUser.getEmail());
        if (actualUser != null &&
                actualUser.getPassword().equals(this.theEncryptionService.convertSHA256(theUser.getPassword()))) {
            token = this.theJwtService.generateToken(actualUser);

        } else {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

        }
        return token;
    }

    /**
     * 2do factor de autenticación
     * 
     * @param Session theSession
     */
@PostMapping("2FA-login")
    public ResponseEntity<?> factorAuthetication(@RequestBody Session theSession){
        try{
            String email = theSession.getTheUser().getEmail(); 
            int secondFactor_token = theSession.getToken2FA();
            User theUser = theUserRepository.getUserByEmail(email);
            Session thePrincipalSession = theSessionRepository.getSessionbyUserId(email, secondFactor_token);
            
            if(thePrincipalSession != null){
                String token = this.theJwtService.generateToken(theUser);
                thePrincipalSession.setToken(token);
                this.theSessionRepository.save(thePrincipalSession);
                this.jsonResponsesService.setData(token);
                this.jsonResponsesService.setMessage("Se ha ingresado exitosamente, el token es:");
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(this.jsonResponsesService.getFinalJSON());
            }else if(theUser != null){
                this.jsonResponsesService.setMessage("Código de autenticación incorrecto.");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(this.jsonResponsesService.getFinalJSON());
            }else{
                this.jsonResponsesService.setMessage("Correo inexistente.");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(this.jsonResponsesService.getFinalJSON());
            }
        }catch (Exception e){
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al buscar usuarios");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

}
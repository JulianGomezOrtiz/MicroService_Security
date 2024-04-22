package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Permission;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Models.Session;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import com.ucaldas.mssecurity.Repositories.SessionRepository;
import com.ucaldas.mssecurity.Services.EncryptionService;
import com.ucaldas.mssecurity.Services.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.ucaldas.mssecurity.Services.ValidatorsService;
import com.ucaldas.mssecurity.Services.JSONResponsesService;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/public/security")
public class SecurityController {

    @Autowired
    private ValidatorsService theValidatorsService;

    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private EncryptionService theEncryptionService;

    @Autowired
    private JwtService theJwtService;

    @Autowired
    private JSONResponsesService theJsonResponsesService;

    @Autowired
    private SessionRepository theSessionRepository;



    @PostMapping("login")
    public ResponseEntity<?> logins(@RequestBody User theUser, final HttpServletResponse response) {
        User actualUser;
        try {
             actualUser = this.theUserRepository.getUserByEmail(theUser.getEmail());
            if (actualUser != null && actualUser.getPassword().equals(this.theEncryptionService.convertSHA256(theUser.getPassword()))) {
                // Crear y guardar la sesión
                int code = generateCode();
                System.out.println(code);
                Session userSession = new Session(true, code);
                userSession.setUser(actualUser);
                theSessionRepository.save(userSession);

                RestTemplate restTemplate= new RestTemplate();
                String url = "http://localhost:5000/" + "send_2FAC";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String requestBody = "{\"email\":\"" + actualUser.getEmail() +"\",\"code\":\"" + code + "\"}";
                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody,headers);
                ResponseEntity<String> res = restTemplate.postForEntity(url, requestEntity, String.class);


            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Credenciales inválidas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el inicio de sesión: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Codigo enviado a: "+actualUser.getEmail());
    }

    private int generateCode() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // el code va a estar entre 1000 y 9999
    }


    @GetMapping("getSessionCode")
    public ResponseEntity<Integer> getSessionCode(@RequestParam String email) {
        User actualUser = theUserRepository.getUserByEmail(email);
        if (actualUser != null) {
            Optional<Session> userSessionOpt = theSessionRepository.findByUserAndActive(actualUser, true);
            if (userSessionOpt.isPresent()) {
                return ResponseEntity.ok(userSessionOpt.get().getCode());
            }
        }
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(null);
    }

    @PostMapping("verifyCode2FA")
    public ResponseEntity<?> verifyCode2FA(@RequestBody Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        int code = (Integer) requestBody.get("code");
        // HACE FALTA TENER EN CUENTA CUANDO EL USUARIO NO ENVIE LOS PARAMETROS REQUERIDOS: EMAIL, CODE
        User actualUser = theUserRepository.getUserByEmail(email);
        if (actualUser == null) {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(false);
        }

        Optional<Session> userSessionOpt = theSessionRepository.findByUserAndActive(actualUser, true);
        if (userSessionOpt.isPresent() && userSessionOpt.get().getCode() == code) {
            String token = "";
            
            if (actualUser != null) {
                token = theJwtService.generateToken(actualUser);
            } else {
                return ResponseEntity.ok("Error al obtener usuario");
            }
            return ResponseEntity.ok(token);
            // return ResponseEntity.ok("Código válido");
        } else {
            return ResponseEntity.ok("Código Incorrecto");
        }
    }

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        User user = theUserRepository.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body("Usuario no encontrado");
        }

        // Generar código de verificación
        int verificationCode = generateCode();

        // Guardar código de verificación en la sesión del usuario
        Session userSession = new Session(false, verificationCode);
        userSession.setUser(user);
        theSessionRepository.save(userSession);

        // Enviar código de verificación por correo electrónico
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:5000/" + "send_reset-password_code";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBodyEmail = "{\"email\":\"" + user.getEmail() + "\",\"code\":\"" + verificationCode + "\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyEmail, headers);
        ResponseEntity<String> res = restTemplate.postForEntity(url, requestEntity, String.class);

        return ResponseEntity.status(HttpStatus.OK).body("Código de verificación enviado a " + user.getEmail());
    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        int verificationCode = (Integer) requestBody.get("code");
        String newPassword = (String) requestBody.get("newPassword");

        User user = theUserRepository.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body("Usuario no encontrado");
        }

        Optional<Session> userSessionOpt = theSessionRepository.findByUserAndActive(user, false);
        if (userSessionOpt.isPresent() && userSessionOpt.get().getCode() == verificationCode) {
            user.setPassword(theEncryptionService.convertSHA256(newPassword));
            theUserRepository.save(user);
            return ResponseEntity.ok("Contraseña cambiada exitosamente");
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Código de verificación incorrecto");
        }
    }


    /**
     * Validacion de permisos
     * 
     * @param
     */

    @PostMapping("permisions-validation")
    public boolean permissionsValidation(final jakarta.servlet.http.HttpServletRequest request,
            @RequestBody Permission ThePermission) {
        boolean success = this.theValidatorsService.validationRolePermission(request, ThePermission.getUrl(),
                ThePermission.getMethod());

        return success;
    }

    @GetMapping("token-validation")
    public User tokenValidation(final HttpServletRequest request) {
        User theUser = this.theValidatorsService.getUser(request);
        return theUser;
    }

}
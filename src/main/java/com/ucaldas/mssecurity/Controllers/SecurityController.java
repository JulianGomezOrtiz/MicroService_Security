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

    @PostMapping("permisions-validation")
    public boolean permissionsValidation(final jakarta.servlet.http.HttpServletRequest request,
            @RequestBody Permission ThePermission) {
        boolean success = this.theValidatorsService.validationRolePermission(request, ThePermission.getUrl(),
                ThePermission.getMethod());

        return success;
    }

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

}
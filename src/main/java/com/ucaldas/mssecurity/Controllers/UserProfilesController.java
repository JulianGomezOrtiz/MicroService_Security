package com.ucaldas.mssecurity.Controllers;


import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Models.UserProfile;
import com.ucaldas.mssecurity.Repositories.UserProfileRepository;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import com.ucaldas.mssecurity.Services.JSONResponsesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users-profiles")
public class UserProfilesController {
    @Autowired
    private UserProfileRepository theProfileRepository;
    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private JSONResponsesService theJsonResponse;

    @PostMapping("user/{id}")
    public ResponseEntity<?> create(@PathVariable String id, @RequestBody UserProfile theProfile){
        try{
            User theUser = theUserRepository.findById(id).orElse(null);
            if(theUser != null){
                theProfile.setTheUser(theUser);
                UserProfile actualProfile = this.theProfileRepository.save(theProfile);

                theJsonResponse.setData(actualProfile);
                theJsonResponse.setMessage("Se ha creado el perfil.");
                return ResponseEntity.status(HttpStatus.OK).body(theJsonResponse.getFinalJSON());
            }else{
                this.theJsonResponse.setMessage("No se encontró usuario.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.theJsonResponse.getFinalJSON());
            }
        }catch (Exception e){
            this.theJsonResponse.setData(null);
            this.theJsonResponse.setMessage("Error al buscar usuario.");
            this.theJsonResponse.setError(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.theJsonResponse.getFinalJSON());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        try{
            UserProfile theProfile = theProfileRepository.findById(id).orElse(null);
            if(theProfile != null){
                this.theProfileRepository.delete(theProfile);
                this.theJsonResponse.setMessage("El perfil se ha eliminado.");
                return ResponseEntity.status(HttpStatus.OK).body(this.theJsonResponse.getFinalJSON());
            }else{
                this.theJsonResponse.setMessage("No se encontró perfil.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.theJsonResponse.getFinalJSON());
            }
        }catch (Exception e){
            this.theJsonResponse.setData(null);
            this.theJsonResponse.setMessage("Error al buscar perfil.");
            this.theJsonResponse.setError(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.theJsonResponse.getFinalJSON());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody UserProfile theNewProfile){
        UserProfile theActualProfile = this.theProfileRepository.findById(id).orElse(null);
        try{
            if(theActualProfile != null){
                theActualProfile.setName(theNewProfile.getName());
                theActualProfile.setLast_name(theNewProfile.getLast_name());
                theActualProfile.setCity_of_residence(theNewProfile.getCity_of_residence());
                theActualProfile.setAddress(theNewProfile.getAddress());
                theActualProfile.setphone_number(theNewProfile.getPhone_number());
                theActualProfile.setBirthday(theNewProfile.getBirthday());
                UserProfile theProfile = this.theProfileRepository.save(theActualProfile);
                theJsonResponse.setData(theProfile);
                theJsonResponse.setMessage("Se ha actualizado el perfil.");
                return ResponseEntity.status(HttpStatus.OK).body(theJsonResponse.getFinalJSON());
            }else{
                this.theJsonResponse.setMessage("No se encontró perfil.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.theJsonResponse.getFinalJSON());
            }
        }catch (Exception e){
            this.theJsonResponse.setData(null);
            this.theJsonResponse.setMessage("Error al buscar perfil.");
            this.theJsonResponse.setError(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.theJsonResponse.getFinalJSON());
        }
    }

    @GetMapping("{id}")
    public UserProfile findByUser(@PathVariable String id){
        try{
            UserProfile theProfile = this.theProfileRepository.getProfilebyUserId(id);
            System.out.println(theProfile);
            if(theProfile != null){
                theJsonResponse.setData(theProfile);
                theJsonResponse.setMessage("Se ha encontrado el perfil.");
                return theProfile;
            }else{
                this.theJsonResponse.setMessage("No se encontró perfil.");
                return theProfile;
            }
        }catch (Exception e){
            this.theJsonResponse.setData(null);
            this.theJsonResponse.setMessage("Error al buscar perfil.");
            this.theJsonResponse.setError(e.toString());
            return null;
        }
    }
}

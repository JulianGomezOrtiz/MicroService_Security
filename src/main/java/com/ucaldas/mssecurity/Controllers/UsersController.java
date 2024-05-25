package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Role;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Repositories.RoleRepository;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import com.ucaldas.mssecurity.Services.JSONResponsesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ucaldas.mssecurity.Services.EncryptionService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JSONResponsesService jsonResponsesService;
    @Autowired
    private RoleRepository theRoleRepository;
    @Autowired
    private EncryptionService thEncryptionService;


    @SuppressWarnings("unused")
    @GetMapping("")
    public ResponseEntity<?> index() {
        try {
            List<User> users = this.userRepository.findAll();
            if (users != null && users.size() > 0) {
                this.jsonResponsesService.setData(users);
                this.jsonResponsesService.setMessage("Lista de Usuarios encontrada correctamente");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setMessage("No hay usuarios registrados");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al buscar usuarios");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

    @PutMapping("{userId}/role/{roleId}")
    public ResponseEntity<?> matchRole(@PathVariable String userId, @PathVariable String roleId) {
        try {
            User theActualUser = this.userRepository
                    .findById(userId)
                    .orElse(null);
            Role theActualRole = this.theRoleRepository
                    .findById(roleId)
                    .orElse(null);
            if (theActualUser != null && theActualRole != null) {
                theActualUser.setRole(theActualRole);
                User newUser = this.userRepository.save(theActualUser);
                this.jsonResponsesService.setData(newUser);
                this.jsonResponsesService.setMessage("Rol asignado al usuario con éxito");
                return ResponseEntity.status(HttpStatus.OK).body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setData(null);
                this.jsonResponsesService.setMessage("No se encontro al usuario o el rol no existe");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setMessage("Error al buscar usuario");
            this.jsonResponsesService.setError(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }

    }

    @PutMapping("{userId}/unmatch-role/{roleId}")
    public ResponseEntity<?> unMatchRole(@PathVariable String userId, @PathVariable String roleId) {
        try {
            User theActualUser = this.userRepository
                    .findById(userId)
                    .orElse(null);
            Role theActualRole = this.theRoleRepository
                    .findById(roleId)
                    .orElse(null);
            if (theActualUser != null
                    && theActualRole != null
                    && theActualUser.getRole().get_id().equals(roleId)) {
                theActualUser.setRole(null);
                User newUser = this.userRepository.save(theActualUser);
                this.jsonResponsesService.setData(newUser);
                this.jsonResponsesService.setMessage("Se designo el rol al usuario con éxito");
                return ResponseEntity.status(HttpStatus.OK).body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setData(null);
                this.jsonResponsesService.setMessage("No se encontro al usuario o el rol no existe");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setMessage("Error al buscar usuario");
            this.jsonResponsesService.setError(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

    @GetMapping("{id}")
    public User findById(@PathVariable String id) { // es ResponseEntity
        try {
            User user = this.userRepository.findById(id).orElse(null);
            if (user != null) {
                this.jsonResponsesService.setData(user);
                this.jsonResponsesService.setMessage("Usuario encontrado con éxito");
                //return ResponseEntity.status(HttpStatus.OK).body(this.jsonResponsesService.getFinalJSON());
                return user;
            } else {
                this.jsonResponsesService.setData(null);
                this.jsonResponsesService.setMessage("No se encontro al usuario buscado");
                return user;
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setMessage("Error al buscar usuario");
            this.jsonResponsesService.setError(e.toString());
            return null;
        }
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody User newUser) {
        try {
            newUser.setPassword(thEncryptionService.convertSHA256(newUser.getPassword()));
            User user = this.userRepository.save(newUser);
            this.jsonResponsesService.setData(user);
            this.jsonResponsesService.setMessage("Usuario añadido satisfactoriamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(this.jsonResponsesService.getFinalJSON());
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al crear al usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            User user = this.userRepository.findById(id).orElse(null);
            if (user != null) {
                this.userRepository.delete(user);
                this.jsonResponsesService.setMessage("Usuario eliminado satisfactoriamente");
                return ResponseEntity.status(HttpStatus.OK).body(this.jsonResponsesService.getFinalJSON());
            } else {
                this.jsonResponsesService.setData(null);
                this.jsonResponsesService.setMessage("No se encontró al usuario");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.jsonResponsesService.getFinalJSON());
            }
        } catch (Exception e) {
            this.jsonResponsesService.setData(null);
            this.jsonResponsesService.setError(e.toString());
            this.jsonResponsesService.setMessage("Error al eliminar al usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(this.jsonResponsesService.getFinalJSON());
        }
    }

}
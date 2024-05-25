package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Permission;
import com.ucaldas.mssecurity.Models.Role;
import com.ucaldas.mssecurity.Models.RolePermission;
import com.ucaldas.mssecurity.Repositories.PermissionRepository;
import com.ucaldas.mssecurity.Repositories.RolePermissionRepository;
import com.ucaldas.mssecurity.Repositories.RoleRepository;
import com.ucaldas.mssecurity.Services.JSONResponsesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/role-permission")
public class RolePermissionController {
    @Autowired
    private RoleRepository theRoleRepository;
    @Autowired
    private PermissionRepository thePermissionRepository;
    @Autowired
    private RolePermissionRepository theRolePermissionRepository;
    @Autowired
    private JSONResponsesService theJsonResponse;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("role/{roleId}/permission/{permissionId}")
    public ResponseEntity<?> create(@PathVariable String roleId, @PathVariable String permissionId) {
        RolePermission theRolePermission = this.theRolePermissionRepository.getRolePermission(roleId, permissionId);
        System.out.println(theRolePermission);
        Role theRole = this.theRoleRepository
                .findById(roleId)
                .orElse(null);
        Permission thePermission = this.thePermissionRepository
                .findById(permissionId)
                .orElse(null);
        try {
            if (theRolePermission != null) {
                this.theJsonResponse.setMessage("El permiso para este rol ya existe");
                return ResponseEntity.status(HttpStatus.OK).body(this.theJsonResponse.getFinalJSON());
            }
            if (theRole != null && thePermission != null && theRolePermission == null) {
                RolePermission newRolePermission = new RolePermission();
                newRolePermission.setRole(theRole);
                newRolePermission.setPermission(thePermission);
                this.theRolePermissionRepository.save(newRolePermission);
                this.theJsonResponse.setData(newRolePermission);
                this.theJsonResponse.setMessage("Se ha creado el permiso para el rol");
                return ResponseEntity.status(HttpStatus.OK).body(this.theJsonResponse.getFinalJSON());
            } else {
                this.theJsonResponse.setMessage("El rol o el permiso no existen");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.theJsonResponse.getFinalJSON());
            }
        } catch (Exception e) {
            this.theJsonResponse.setData(null);
            this.theJsonResponse.setMessage("Error al crear el permiso de el rol");
            this.theJsonResponse.setError(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.theJsonResponse.getFinalJSON());
        }
    }

    @PutMapping("role/{roleId}/permission/{permissionId}")
    public ResponseEntity<?> update(@PathVariable String roleId, @PathVariable String permissionId) {
        Role theRole = this.theRoleRepository
                .findById(roleId)
                .orElse(null);
        Permission thePermission = this.thePermissionRepository
                .findById(permissionId)
                .orElse(null);
        RolePermission theRolePermission = this.theRolePermissionRepository.getRolePermission(roleId, permissionId);
        try {
            if (theRolePermission == null) {
                this.theJsonResponse.setMessage("El permiso de este rol no existe");
                return ResponseEntity.status(HttpStatus.OK).body(this.theJsonResponse.getFinalJSON());
            }
            if (theRole != null && thePermission != null && theRolePermission != null) {
                RolePermission newRolePermission = new RolePermission();
                newRolePermission.setRole(theRole);
                newRolePermission.setPermission(thePermission);
                this.theRolePermissionRepository.save(newRolePermission);
                this.theJsonResponse.setData(newRolePermission);
                this.theJsonResponse.setMessage("Se ha actulizado el permiso del rol");
                return ResponseEntity.status(HttpStatus.OK).body(this.theJsonResponse.getFinalJSON());
            } else {
                this.theJsonResponse.setMessage("El rol o el permiso no existen");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.theJsonResponse.getFinalJSON());
            }
        } catch (Exception e) {
            this.theJsonResponse.setData(null);
            this.theJsonResponse.setMessage("Error al actualizar el permiso de el rol");
            this.theJsonResponse.setError(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.theJsonResponse.getFinalJSON());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        RolePermission theRolePermission = this.theRolePermissionRepository
                .findById(id)
                .orElse(null);
        try {
            if (theRolePermission != null) {
                this.theRolePermissionRepository.delete(theRolePermission);
                this.theJsonResponse.setData(theRolePermission);
                this.theJsonResponse.setMessage("Se ha eliminado el permiso para el rol");
                return ResponseEntity.status(HttpStatus.OK).body(this.theJsonResponse.getFinalJSON());
            } else {
                this.theJsonResponse.setMessage("No se encontró el permiso o el rol, para eliminar el permiso del rol");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.theJsonResponse.getFinalJSON());
            }
        } catch (Exception e) {
            this.theJsonResponse.setData(null);
            this.theJsonResponse.setMessage("Error al buscar el permiso a eliminar del rol.");
            this.theJsonResponse.setError(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.theJsonResponse.getFinalJSON());
        }
    }

    @GetMapping("role/{roleId}")
    public ResponseEntity<?> findByRole(@PathVariable String roleId) {
        Role theRole = this.theRoleRepository
                .findById(roleId)
                .orElse(null);
        try {
            if (theRole != null) {
                List<RolePermission> permissions = theRolePermissionRepository.getPermissionByRole(roleId);
                if (permissions.size() == 0) {
                    this.theJsonResponse.setMessage("Este rol no tiene permisos asignados");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.theJsonResponse.getFinalJSON());
                } else {
                    this.theJsonResponse.setData(permissions);
                    this.theJsonResponse.setMessage("Los permisos de este rol han sido encontrados");
                    return ResponseEntity.status(HttpStatus.OK).body(this.theJsonResponse.getFinalJSON());
                }
            } else {
                this.theJsonResponse.setMessage("No se encontró el rol");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.theJsonResponse.getFinalJSON());
            }
        } catch (Exception e) {
            this.theJsonResponse.setData(null);
            this.theJsonResponse.setMessage("Error al buscar los permisos del rol.");
            this.theJsonResponse.setError(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.theJsonResponse.getFinalJSON());
        }
    }
}

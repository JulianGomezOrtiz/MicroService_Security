package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Permission;
import com.ucaldas.mssecurity.Models.Role;
import com.ucaldas.mssecurity.Models.RolePermission;
import com.ucaldas.mssecurity.Repositories.PermissionRepository;
import com.ucaldas.mssecurity.Repositories.RolePermissionRepository;
import com.ucaldas.mssecurity.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @ResponseStatus(HttpStatus.CREATED)

    @PostMapping("/role/{roleId}/permission/{permissionId}")
    public RolePermission create(@PathVariable String roleId, @PathVariable String permissionId) {
        Role theRole = this.theRoleRepository.findById(roleId).orElse(null);
        Permission thePermission = this.thePermissionRepository.findById(permissionId).orElse(null);
        if (theRole != null && thePermission != null) {
            RolePermission newRolePermission = new RolePermission();
            newRolePermission.setRole(theRole);
            newRolePermission.setPermission(thePermission);
            return this.theRolePermissionRepository.save(newRolePermission);
        } else {
            return null;
        }
    }

    // Muestra un solo permiso (GET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        RolePermission theRolePermission = this.theRolePermissionRepository
                .findById(id)
                .orElse(null);
        if (theRolePermission != null) {
            this.theRolePermissionRepository.delete(theRolePermission);
        }
    }

    // Muestra todos los permisos (GET)
    @GetMapping("")
    public List<RolePermission> index() {
        return this.theRolePermissionRepository.findAll();
    }

    // Método borrar (DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id) {
        RolePermission theRoleRolePermission = this.theRolePermissionRepository
                .findById(id)
                .orElse(null);
        if (theRoleRolePermission != null) {
            this.theRolePermissionRepository.delete(theRoleRolePermission);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("role/{role_id}/permissions")
    public void createList(@RequestBody List<Permission> ListPermission, @PathVariable String role_id) {

        for (Permission permission : ListPermission) {
            System.out.println(permission.get_id());
            this.create(role_id, permission.get_id());

        }

    }

    public Permission getPermission(String url, String method) {
        List<Permission> permissions = thePermissionRepository.findAll();
        for (Permission permission : permissions) {
            if (permission.getMethod().equals(method) && permission.getUrl().equals(url)) {
                return permission;
            }
        }
        return null;
    }

    // Método DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("all")
    public void destroyAll() {
        List<RolePermission> list = this.theRolePermissionRepository.findAll();
        for (RolePermission rolePermission : list) {
            this.theRolePermissionRepository.delete(rolePermission);
        }

    }

    public PermissionRepository getThePermissionRepository() {
        return thePermissionRepository;
    }

    @GetMapping("role/{roleId}")
    public List<RolePermission> findByRole(@PathVariable String roleId) {
        return this.theRolePermissionRepository.getPermissionByRole(roleId);
    }
}

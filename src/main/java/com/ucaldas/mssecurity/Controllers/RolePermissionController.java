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

import java.util.ArrayList;
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

    // Método POST
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("role/{role_id}/permission/{permission_id}")
    public RolePermission store(@PathVariable String role_id,
            @PathVariable String permission_id) {
        Role theRole = theRoleRepository.findById(role_id).orElse(null);
        Permission thePermission = thePermissionRepository.findById(permission_id).orElse(null);
        if (theRole != null && thePermission != null) {
            RolePermission newRolePermission = new RolePermission();
            newRolePermission.setRole(theRole);
            newRolePermission.setPermission(thePermission);
            return this.theRolePermissionRepository.save(newRolePermission);
        } else {
            return null;
        }
    }

    // Método GET (UNO)
    @GetMapping("{id}")
    public RolePermission show(@PathVariable String id) {
        RolePermission theRolePermission = this.theRolePermissionRepository
                .findById(id)
                .orElse(null);
        return theRolePermission;
    }

    // Método GET (TODOS)
    @GetMapping("")
    public List<RolePermission> index() {
        return this.theRolePermissionRepository.findAll();
    }

    // Método DELETE
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
    public List<RolePermission> storeList(@RequestBody List<Permission> ListPermission, @PathVariable String role_id) {
        List<RolePermission> savedRolePermissions = new ArrayList<>();
        for (Permission permission : ListPermission) {
            System.out.println(permission.get_id());
            RolePermission savedPermission = this.store(role_id, permission.get_id());
            savedRolePermissions.add(savedPermission);
        }
        return savedRolePermissions;
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
}

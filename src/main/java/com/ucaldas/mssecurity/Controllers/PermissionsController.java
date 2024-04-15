package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Permission;
import com.ucaldas.mssecurity.Repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/permissions")
public class PermissionsController {
    @Autowired
    private PermissionRepository thePermissionRepository;

    /**
     * Listado de permisos
     * 
     * @return listado de objetos de tipo Permission
     */
    @GetMapping("")
    public List<Permission> index() {
        return this.thePermissionRepository.findAll();
    }

    /**
     * Crear un permiso
     * 
     * @param newPermission Objeto de Permission
     * @return el permiso guardado
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Permission store(@RequestBody Permission newPermission) {
        return this.thePermissionRepository.save(newPermission);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("list")
    public List<Permission> storeList(@RequestBody List<Permission> ListPermission) {
        List<Permission> savedPermissions = new ArrayList<>();
        for (Permission permission : ListPermission) {
            Permission savedPermission = this.thePermissionRepository.save(permission);
            savedPermissions.add(savedPermission);
        }
        ;
        return savedPermissions;
    }

    /**
     * Muestra un solo permiso
     * 
     * @param id identificador del permiso
     * @return un objeto de tipo Permission
     */
    @GetMapping("{id}")
    public Permission show(@PathVariable String id) {
        Permission thePermission = this.thePermissionRepository
                .findById(id)
                .orElse(null);
        return thePermission;
    }

    /**
     * Actualizar un permiso
     * 
     * @param id               identificador de un permiso
     * @param theNewPermission el objeto actualizado
     * @return null || el permiso
     */
    @PutMapping("{id}")
    public Permission update(@PathVariable String id, @RequestBody Permission theNewPermission) {
        Permission theActualPermission = this.thePermissionRepository
                .findById(id)
                .orElse(null);
        if (theActualPermission != null) {
            theActualPermission.setUrl(theNewPermission.getUrl());
            theActualPermission.setMethod(theNewPermission.getMethod());
            theActualPermission.setDescription(theNewPermission.getDescription());
            return this.thePermissionRepository.save(theActualPermission);
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("all")
    public void destroyAll() {
        List<Permission> thePermissions = this.index();
        for (Permission permission : thePermissions) {
            this.thePermissionRepository.delete(permission);
        }
    }

    /**
     * Eliminar un permiso
     * 
     * @param id identificador del permiso
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id) {
        Permission thePermission = this.thePermissionRepository
                .findById(id)
                .orElse(null);
        if (thePermission != null) {
            this.thePermissionRepository.delete(thePermission);
        }
    }

}
package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Role;
import com.ucaldas.mssecurity.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/roles")
public class RolesController {
    @Autowired
    private RoleRepository theRoleRepository;

    /**
     * Metodo para listar los roles
     * 
     * @return listado de objetos de tipo Role
     */

    @GetMapping("")
    public List<Role> findAll() {
        return this.theRoleRepository.findAll();
    }

    /**
     * Metodo para crear un rol
     * 
     *
     * @return el rol guardado
     */

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Role create(@RequestBody Role theNewRole) {
        return this.theRoleRepository.save(theNewRole);
    }

    /**
     * Metodo para mostrar un solo rol
     * 
     * @param id identificador del rol
     * @return un objeto de tipo Role
     */

    @GetMapping("{id}")
    public Role findById(@PathVariable String id) {
        Role theRole = this.theRoleRepository
                .findById(id)
                .orElse(null);
        return theRole;
    }

    /**
     * Metodo para ctualizar un rol
     * 
     * @param id         identificador de un rol
     * @param theNewRole el objeto actualizado
     * @return null || el rol
     */

    @PutMapping("{id}")
    public Role update(@PathVariable String id, @RequestBody Role theNewRole) {
        Role theActualRole = this.theRoleRepository
                .findById(id)
                .orElse(null);
        if (theActualRole != null) {
            theActualRole.setName(theNewRole.getName());
            theActualRole.setDescription(theNewRole.getDescription());

            return this.theRoleRepository.save(theActualRole);
        } else {
            return null;
        }
    }

    /**
     * Metodo para eliminar un rol
     * 
     * @param id identificador del rol
     */

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Role theRole = this.theRoleRepository
                .findById(id)
                .orElse(null);
        if (theRole != null) {
            this.theRoleRepository.delete(theRole);
        }
    }
}

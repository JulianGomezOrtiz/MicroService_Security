package com.ucaldas.mssecurity.Controllers;


import com.ucaldas.mssecurity.Models.Fidelidad;
import com.ucaldas.mssecurity.Repositories.Fidelidadrepository;
import com.ucaldas.mssecurity.Repositories.Fidelidadrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/fidelidad")
public class FidelidadesController {

    @Autowired
    private Fidelidadrepository theFidelidadrepository;

    @GetMapping("")
    public List<Fidelidad> findAll() {
        return this.theFidelidadrepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)

    @PostMapping
    public Fidelidad create(@RequestBody Fidelidad theNewFidelidad) {
        return this.theFidelidadrepository.save(theNewFidelidad);
    }

    @GetMapping("{id}")
    public Fidelidad findById(@PathVariable String id) {
        Fidelidad theFidelidad = this.theFidelidadrepository
                .findById(id)
                .orElse(null);
        return theFidelidad;

    }

    @PutMapping("{id}")
    public Fidelidad update(@PathVariable String id, @RequestBody Fidelidad theNewFidelidad) {
        Fidelidad theActualFidelidad = this.theFidelidadrepository
                .findById(id)
                .orElse(null);
        if (theActualFidelidad != null) {
            theActualFidelidad.setName(theNewFidelidad.getName());
            theActualFidelidad.setLast_name(theNewFidelidad.getLast_name());
            theActualFidelidad.setCity_of_residence(theNewFidelidad.getCity_of_residence());
            theActualFidelidad.setAddress(theNewFidelidad.getAddress());
            theActualFidelidad.setphone_number(theNewFidelidad.getPhone_number());
            return this.theFidelidadrepository.save(theActualFidelidad);
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Fidelidad theUser = this.theFidelidadrepository
                .findById(id)
                .orElse(null);
        if (theUser != null) {
            this.theFidelidadrepository.delete(theUser);
        }
    }
}

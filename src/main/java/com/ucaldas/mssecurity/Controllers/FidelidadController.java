package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Fidelidad;
import com.ucaldas.mssecurity.Models.Session;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Repositories.FidelidadRepository;
import com.ucaldas.mssecurity.Repositories.SessionRepository;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("fidelidades")
public class FidelidadController {

    @Autowired
    private FidelidadRepository theFidelidadRepository;


    /**
     * Listado de sesiones
     *
     * @return listado de objetos de tipo Session
     */
    @GetMapping("")
    public List<Fidelidad> index() {
        return this.theFidelidadRepository.findAll();
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("create")
    public Fidelidad store(@RequestBody Fidelidad newFidelidad) {
        System.out.println(newFidelidad);
        return this.theFidelidadRepository.save(newFidelidad);
    }

    /**
     * Mostrar un solo session
     *
     * @param id identificador del session
     * @return un objeto de tipo Session
     */
    @GetMapping("{id}")
    public Fidelidad show(@PathVariable String id) {
        Fidelidad theFidelidad = this.theFidelidadRepository
                .findById(id)
                .orElse(null);
        return theFidelidad;
    }



    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id) {
        Fidelidad thefidelidad = this.theFidelidadRepository
                .findById(id)
                .orElse(null);
        if (thefidelidad != null) {
            this.theFidelidadRepository.delete(thefidelidad);
        }
    }



}

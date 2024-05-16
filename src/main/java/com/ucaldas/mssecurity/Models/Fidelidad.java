package com.ucaldas.mssecurity.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Fidelidad {
    @Id
    private String _id;
    private int puntos;

    public Fidelidad(int puntos) {
        this.puntos = puntos;
    }

    public Fidelidad(){}

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getPuntos() {
        return puntos;
    }

    public String get_id() {
        return _id;
    }

}

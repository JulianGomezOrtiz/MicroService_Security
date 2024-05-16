package com.ucaldas.mssecurity.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ucaldas.mssecurity.Models.Fidelidad;
import com.ucaldas.mssecurity.Models.User;

public class Fidelidadrepository {
    public interface theFidelidadrepository extends MongoRepository<Fidelidad, Integer> {
    @Query("{'puntos': ?0}")
    public User getFide(Integer puntos);
}
}

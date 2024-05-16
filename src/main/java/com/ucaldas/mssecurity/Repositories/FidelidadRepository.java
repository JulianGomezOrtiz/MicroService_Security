package com.ucaldas.mssecurity.Repositories;

import com.ucaldas.mssecurity.Models.Fidelidad;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FidelidadRepository extends MongoRepository<Fidelidad, String> {
}

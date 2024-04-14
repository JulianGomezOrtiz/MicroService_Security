package com.ucaldas.mssecurity.Repositories;

import com.ucaldas.mssecurity.Models.Session;
import com.ucaldas.mssecurity.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends MongoRepository<Session, String> {

    // Realiza una busqueda de una sesión activa para un usuario
    Optional<Session> findByUserAndActive(User user, boolean active);

    // Busca todas las sesiones activas de un usuario
    @Query("{ 'user._id': ?0 }")
    List<Session> findByUser_Id(String userId);
}
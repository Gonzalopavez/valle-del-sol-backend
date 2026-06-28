package com.valledelsol.incident_service.repository;

import com.valledelsol.incident_service.model.Incident;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends MongoRepository<Incident, String> {
    // Spring Data Mongo se encarga de todo de forma nativa
    long countByUserId(String userId);
}

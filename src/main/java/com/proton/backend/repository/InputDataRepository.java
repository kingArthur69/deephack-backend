package com.proton.backend.repository;

import com.proton.backend.model.InputData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputDataRepository extends MongoRepository<InputData, String> {
}

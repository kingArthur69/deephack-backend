package com.proton.backend.repository;

import com.proton.backend.model.InputData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InputDataRepository extends MongoRepository<InputData, String> {

    List<InputData> findAllByMeterIdAndDateTimeBetweenOrderByDateTime(Long meterId, LocalDateTime startDate, LocalDateTime endDate);
}

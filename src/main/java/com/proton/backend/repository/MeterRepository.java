package com.proton.backend.repository;

import com.proton.backend.model.Meter;
import com.proton.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeterRepository extends JpaRepository<Meter, Long> {

    List<Meter> findAllByUser(User user);
}

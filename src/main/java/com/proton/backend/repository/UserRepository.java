package com.proton.backend.repository;

import com.proton.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByIdnp(String idnp);
}

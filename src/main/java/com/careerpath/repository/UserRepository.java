package com.careerpath.repository;

import com.careerpath.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for database operations on the User entity.
 * JpaRepository automatically provides save(), findById(), delete() etc.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method: Spring Data JPA automatically generates the SQL to find a user by username
    Optional<User> findByUsername(String username);
    
    // Custom query method: checks if a username already exists in the database
    boolean existsByUsername(String username);

    // Custom query method: checks if an email already exists in the database
    boolean existsByEmail(String email);
}

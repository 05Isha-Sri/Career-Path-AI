package com.careerpath.repository;

import com.careerpath.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for database operations on the Company entity.
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
    
    // Finds a company by name (useful for checks)
    Optional<Company> findByName(String name);
}

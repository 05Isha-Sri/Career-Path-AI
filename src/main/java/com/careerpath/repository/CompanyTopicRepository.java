package com.careerpath.repository;

import com.careerpath.entity.CompanyTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for database operations on CompanyTopic entity.
 */
public interface CompanyTopicRepository extends JpaRepository<CompanyTopic, Long> {

    // Fetch all topics associated with a company
    List<CompanyTopic> findByCompanyId(Long companyId);
}

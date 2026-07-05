package com.careerpath.repository;

import com.careerpath.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for database operations on Resource entity.
 */
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // Fetch study resources by topic name (e.g. 'OOP', 'DBMS')
    List<Resource> findByTopicName(String topicName);
}

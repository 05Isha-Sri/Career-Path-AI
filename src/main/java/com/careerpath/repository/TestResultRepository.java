package com.careerpath.repository;

import com.careerpath.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for database operations on TestResult entity.
 */
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    // Fetch test results for a specific student, showing the latest tests first
    List<TestResult> findByUserIdOrderByCompletedAtDesc(Long userId);
}

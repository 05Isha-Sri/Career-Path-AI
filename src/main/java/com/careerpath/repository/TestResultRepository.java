package com.careerpath.repository;

import com.careerpath.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    List<TestResult> findByUserIdOrderByCompletedAtDesc(Long userId);
}

package com.careerpath.repository;

import com.careerpath.entity.CodingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for database operations on CodingQuestion entity.
 */
public interface CodingQuestionRepository extends JpaRepository<CodingQuestion, Long> {

    // Fetch coding questions filtered by difficulty: BEGINNER, INTERMEDIATE, ADVANCED
    List<CodingQuestion> findByDifficulty(String difficulty);
}

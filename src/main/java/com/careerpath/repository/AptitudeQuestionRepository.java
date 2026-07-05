package com.careerpath.repository;

import com.careerpath.entity.AptitudeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * Repository interface for database operations on AptitudeQuestion entity.
 */
public interface AptitudeQuestionRepository extends JpaRepository<AptitudeQuestion, Long> {

    // Fetch questions by category: QUANTITATIVE, LOGICAL, VERBAL
    List<AptitudeQuestion> findByCategory(String category);

    // Fetch a set of random questions from the database for the mock test.
    // 'value' holds the raw SQL query. 'nativeQuery = true' tells JPA to execute it exactly as written.
    @Query(value = "SELECT * FROM aptitude_questions ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<AptitudeQuestion> findRandomQuestions(int limit);
}

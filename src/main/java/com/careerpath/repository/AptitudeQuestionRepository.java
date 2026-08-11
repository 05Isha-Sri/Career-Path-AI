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

    // Fetch generic questions (where company_id is NULL) by category
    List<AptitudeQuestion> findByCompanyIdIsNullAndCategory(String category);

    // Fetch all generic questions (where company_id is NULL)
    List<AptitudeQuestion> findByCompanyIdIsNull();

    // Fetch questions filtered by company and category
    List<AptitudeQuestion> findByCompanyIdAndCategory(Long companyId, String category);

    // Fetch all questions for a specific company
    List<AptitudeQuestion> findByCompanyId(Long companyId);

    // Fetch a set of random questions from the database for the mock test.
    // 'value' holds the raw SQL query. 'nativeQuery = true' tells JPA to execute it exactly as written.
    @Query(value = "SELECT * FROM aptitude_questions ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<AptitudeQuestion> findRandomQuestions(int limit);

    // Fetch random questions specifically for a company (with fallback to generic questions where company_id is NULL)
    @Query(value = "SELECT * FROM aptitude_questions WHERE company_id = ?1 OR company_id IS NULL ORDER BY RAND() LIMIT ?2", nativeQuery = true)
    List<AptitudeQuestion> findRandomQuestionsByCompany(Long companyId, int limit);
}

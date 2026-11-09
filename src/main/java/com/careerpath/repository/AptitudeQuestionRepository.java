package com.careerpath.repository;

import com.careerpath.entity.AptitudeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface AptitudeQuestionRepository extends JpaRepository<AptitudeQuestion, Long> {

    List<AptitudeQuestion> findByCategory(String category);

    List<AptitudeQuestion> findByCompanyIdIsNullAndCategory(String category);

    List<AptitudeQuestion> findByCompanyIdIsNull();

    List<AptitudeQuestion> findByCompanyIdAndCategory(Long companyId, String category);

    List<AptitudeQuestion> findByCompanyId(Long companyId);

    @Query(value = "SELECT * FROM aptitude_questions ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<AptitudeQuestion> findRandomQuestions(int limit);

    @Query(value = "SELECT * FROM aptitude_questions WHERE company_id = ?1 ORDER BY RAND() LIMIT ?2", nativeQuery = true)
    List<AptitudeQuestion> findRandomQuestionsByCompany(Long companyId, int limit);
}

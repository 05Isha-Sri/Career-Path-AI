package com.careerpath.repository;

import com.careerpath.entity.CodingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CodingQuestionRepository extends JpaRepository<CodingQuestion, Long> {

    List<CodingQuestion> findByDifficulty(String difficulty);
}

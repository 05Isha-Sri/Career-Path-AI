package com.careerpath.controller;

import com.careerpath.entity.AptitudeQuestion;
import com.careerpath.entity.CodingQuestion;
import com.careerpath.entity.TestResult;
import com.careerpath.entity.User;
import com.careerpath.repository.AptitudeQuestionRepository;
import com.careerpath.repository.CodingQuestionRepository;
import com.careerpath.repository.TestResultRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


import com.careerpath.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class PracticeController {

    @Autowired
    private AptitudeQuestionRepository aptitudeQuestionRepository;

    @Autowired
    private CodingQuestionRepository codingQuestionRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private UserRepository userRepository;

    
    @GetMapping("/aptitude/questions")
    public ResponseEntity<List<AptitudeQuestion>> getAptitudeQuestions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long companyId,
            HttpSession session) {
        
        com.careerpath.entity.User loggedUser = (com.careerpath.entity.User) session.getAttribute("user");
        Long targetCompanyId = companyId;

        if (targetCompanyId == null && loggedUser != null) {
            Optional<User> freshUser = userRepository.findById(loggedUser.getId());
            if (freshUser.isPresent()) {
                targetCompanyId = freshUser.get().getSelectedCompanyId();
            } else {
                targetCompanyId = loggedUser.getSelectedCompanyId();
            }
        }

        List<AptitudeQuestion> questions;

        if (targetCompanyId != null) {
            if (category != null && !category.trim().isEmpty()) {
                questions = aptitudeQuestionRepository.findByCompanyIdAndCategory(targetCompanyId, category.toUpperCase());
            } else {
                questions = aptitudeQuestionRepository.findByCompanyId(targetCompanyId);
            }
        } else {
            if (category != null && !category.trim().isEmpty()) {
                questions = aptitudeQuestionRepository.findByCompanyIdIsNullAndCategory(category.toUpperCase());
            } else {
                questions = aptitudeQuestionRepository.findByCompanyIdIsNull();
            }
        }

        return ResponseEntity.ok(questions);
    }

    
    @GetMapping("/aptitude/mock")
    public ResponseEntity<List<AptitudeQuestion>> getMockTestQuestions(
            @RequestParam(required = false) Long companyId,
            HttpSession session) {
        com.careerpath.entity.User loggedUser = (com.careerpath.entity.User) session.getAttribute("user");
        Long targetCompanyId = companyId;

        if (targetCompanyId == null && loggedUser != null) {
            Optional<User> freshUser = userRepository.findById(loggedUser.getId());
            if (freshUser.isPresent()) {
                targetCompanyId = freshUser.get().getSelectedCompanyId();
            } else {
                targetCompanyId = loggedUser.getSelectedCompanyId();
            }
        }

        List<AptitudeQuestion> questions;

        if (targetCompanyId != null) {
            // Strictly fetch practice questions belonging to the specific target company
            List<AptitudeQuestion> companyQuestions = new ArrayList<>(aptitudeQuestionRepository.findByCompanyId(targetCompanyId));
            
            // Randomize question order every time a new mock assessment starts
            Collections.shuffle(companyQuestions);

            // Select 15 questions, or all available if fewer than 15
            if (companyQuestions.size() > 15) {
                questions = new ArrayList<>(companyQuestions.subList(0, 15));
            } else {
                questions = companyQuestions;
            }
        } else {
            // Do NOT return common questions across all companies
            questions = Collections.emptyList();
        }
        return ResponseEntity.ok(questions);
    }

    
    @PostMapping("/aptitude/mock/submit")
    public ResponseEntity<?> submitMockTest(@RequestBody Map<String, String> answers, HttpSession session) {
        
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unauthorized. Please log in first.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        int score = 0;
        int totalQuestions = answers.size();

        if (totalQuestions == 0) {
            TestResult emptyResult = new TestResult(loggedUser.getId(), 0, 0, 0.0);
            TestResult savedResult = testResultRepository.save(emptyResult);
            return ResponseEntity.ok(savedResult);
        }

        for (Map.Entry<String, String> entry : answers.entrySet()) {
            try {
                Long questionId = Long.parseLong(entry.getKey());
                String selectedOption = entry.getValue();

                if (selectedOption != null && !selectedOption.trim().isEmpty()) {
                    Optional<AptitudeQuestion> questionOpt = aptitudeQuestionRepository.findById(questionId);
                    if (questionOpt.isPresent()) {
                        AptitudeQuestion question = questionOpt.get();
                        
                        if (question.getCorrectOption().trim().equalsIgnoreCase(selectedOption.trim())) {
                            score++;
                        }
                    }
                }
            } catch (NumberFormatException e) {
            }
        }

        double percentage = ((double) score / totalQuestions) * 100.0;
        
        percentage = Math.round(percentage * 100.0) / 100.0;

        TestResult result = new TestResult(loggedUser.getId(), score, totalQuestions, percentage);
        TestResult savedResult = testResultRepository.save(result);

        return ResponseEntity.ok(savedResult);
    }

    
    @GetMapping("/aptitude/results")
    public ResponseEntity<?> getTestResults(HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unauthorized.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        List<TestResult> results = testResultRepository.findByUserIdOrderByCompletedAtDesc(loggedUser.getId());
        return ResponseEntity.ok(results);
    }

    
    @GetMapping("/coding/questions")
    public ResponseEntity<List<CodingQuestion>> getCodingQuestions(
            @RequestParam(required = false) String difficulty) {
        
        if (difficulty != null && !difficulty.trim().isEmpty()) {
            return ResponseEntity.ok(codingQuestionRepository.findByDifficulty(difficulty.toUpperCase()));
        }
        return ResponseEntity.ok(codingQuestionRepository.findAll());
    }

    
    @GetMapping("/coding/questions/{id}")
    public ResponseEntity<?> getCodingQuestionById(@PathVariable Long id) {
        Optional<CodingQuestion> questionOpt = codingQuestionRepository.findById(id);
        if (questionOpt.isPresent()) {
            return ResponseEntity.ok(questionOpt.get());
        }

        Map<String, String> response = new HashMap<>();
        response.put("error", "Coding question not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

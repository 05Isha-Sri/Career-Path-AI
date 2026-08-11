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

/**
 * Controller handling Practice Questions, Mock Tests, and Coding Questions.
 */
@RestController
@RequestMapping("/api")
public class PracticeController {

    @Autowired
    private AptitudeQuestionRepository aptitudeQuestionRepository;

    @Autowired
    private CodingQuestionRepository codingQuestionRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    // ===================================================================
    // Aptitude Practice Methods
    // ===================================================================

    /**
     * Fetch aptitude questions, filtered by the student's selected company and category.
     * If the company has no specific questions, it falls back to generic questions.
     */
    @GetMapping("/aptitude/questions")
    public ResponseEntity<List<AptitudeQuestion>> getAptitudeQuestions(
            @RequestParam(required = false) String category, HttpSession session) {
        
        com.careerpath.entity.User loggedUser = (com.careerpath.entity.User) session.getAttribute("user");
        List<AptitudeQuestion> questions;

        // If user has selected a company, fetch ONLY that company's questions (no leak, no fallback)
        if (loggedUser != null && loggedUser.getSelectedCompanyId() != null) {
            Long companyId = loggedUser.getSelectedCompanyId();
            if (category != null && !category.trim().isEmpty()) {
                questions = aptitudeQuestionRepository.findByCompanyIdAndCategory(companyId, category.toUpperCase());
            } else {
                questions = aptitudeQuestionRepository.findByCompanyId(companyId);
            }
        } else {
            // If no company is selected, load generic questions (where company_id is NULL)
            if (category != null && !category.trim().isEmpty()) {
                questions = aptitudeQuestionRepository.findByCompanyIdIsNullAndCategory(category.toUpperCase());
            } else {
                questions = aptitudeQuestionRepository.findByCompanyIdIsNull();
            }
        }

        return ResponseEntity.ok(questions);
    }

    /**
     * Generate a Mock Test.
     * Fetches 10 random questions matching the user's selected company (with fallback to generic).
     */
    @GetMapping("/aptitude/mock")
    public ResponseEntity<List<AptitudeQuestion>> getMockTestQuestions(HttpSession session) {
        com.careerpath.entity.User loggedUser = (com.careerpath.entity.User) session.getAttribute("user");
        List<AptitudeQuestion> questions;

        if (loggedUser != null && loggedUser.getSelectedCompanyId() != null) {
            // Fetch 10 random questions matching selected company or generic
            questions = aptitudeQuestionRepository.findRandomQuestionsByCompany(loggedUser.getSelectedCompanyId(), 10);
        } else {
            // Fetch 10 general random questions
            questions = aptitudeQuestionRepository.findRandomQuestions(10);
        }
        return ResponseEntity.ok(questions);
    }

    /**
     * Submit Mock Test answers and calculate score.
     * Takes a Map of { "questionId": "selectedOption" } e.g., { "1": "C", "5": "A" }
     */
    @PostMapping("/aptitude/mock/submit")
    public ResponseEntity<?> submitMockTest(@RequestBody Map<String, String> answers, HttpSession session) {
        // Verify if user is logged in
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unauthorized. Please log in first.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        int score = 0;
        int totalQuestions = answers.size();

        // If the student submitted a test with no answers, return early
        if (totalQuestions == 0) {
            TestResult emptyResult = new TestResult(loggedUser.getId(), 0, 0, 0.0);
            TestResult savedResult = testResultRepository.save(emptyResult);
            return ResponseEntity.ok(savedResult);
        }

        // Loop through all submitted answers and check correctness
        for (Map.Entry<String, String> entry : answers.entrySet()) {
            try {
                Long questionId = Long.parseLong(entry.getKey());
                String selectedOption = entry.getValue();

                Optional<AptitudeQuestion> questionOpt = aptitudeQuestionRepository.findById(questionId);
                if (questionOpt.isPresent()) {
                    AptitudeQuestion question = questionOpt.get();
                    // Compare correct option (case-insensitive)
                    if (question.getCorrectOption().trim().equalsIgnoreCase(selectedOption.trim())) {
                        score++;
                    }
                }
            } catch (NumberFormatException e) {
                // Ignore invalid keys
            }
        }

        // Calculate score percentage
        double percentage = ((double) score / totalQuestions) * 100.0;
        // Round to 2 decimal places
        percentage = Math.round(percentage * 100.0) / 100.0;

        // Save the test results in the MySQL database
        TestResult result = new TestResult(loggedUser.getId(), score, totalQuestions, percentage);
        TestResult savedResult = testResultRepository.save(result);

        return ResponseEntity.ok(savedResult);
    }

    /**
     * Retrieve the mock test result history for the logged-in student.
     */
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

    // ===================================================================
    // Coding Practice Methods
    // ===================================================================

    /**
     * Retrieve all coding questions, optionally filtered by difficulty (BEGINNER, INTERMEDIATE, ADVANCED).
     */
    @GetMapping("/coding/questions")
    public ResponseEntity<List<CodingQuestion>> getCodingQuestions(
            @RequestParam(required = false) String difficulty) {
        
        if (difficulty != null && !difficulty.trim().isEmpty()) {
            return ResponseEntity.ok(codingQuestionRepository.findByDifficulty(difficulty.toUpperCase()));
        }
        return ResponseEntity.ok(codingQuestionRepository.findAll());
    }

    /**
     * Fetch a specific coding question details.
     */
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

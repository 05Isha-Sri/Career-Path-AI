package com.careerpath.controller;

import com.careerpath.entity.*;
import com.careerpath.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller for managing and tracking Student Progress (Roadmap completion, solved coding questions, test scores).
 */
@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private CompanyTopicRepository companyTopicRepository;

    @Autowired
    private CodingQuestionRepository codingQuestionRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Mark an item (Topic or Coding question) as completed.
     */
    @PostMapping("/complete")
    public ResponseEntity<?> completeItem(@RequestBody Map<String, String> payload, HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unauthorized. Please log in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String itemType = payload.get("itemType"); // 'TOPIC' or 'CODING'
        String itemKey = payload.get("itemKey");   // topic name or coding question ID

        if (itemType == null || itemKey == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "itemType and itemKey are required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if progress already exists to avoid duplicate entries
        Optional<UserProgress> existing = userProgressRepository.findByUserIdAndItemTypeAndItemKey(
                loggedUser.getId(), itemType, itemKey);

        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get()); // already completed
        }

        // Create and save new progress entry
        UserProgress progress = new UserProgress(loggedUser.getId(), itemType, itemKey);
        UserProgress saved = userProgressRepository.save(progress);

        return ResponseEntity.ok(saved);
    }

    /**
     * Unmark an item (Topic or Coding question) as completed (mark as incomplete).
     */
    @PostMapping("/incomplete")
    public ResponseEntity<?> incompleteItem(@RequestBody Map<String, String> payload, HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unauthorized.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String itemType = payload.get("itemType");
        String itemKey = payload.get("itemKey");

        if (itemType == null || itemKey == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "itemType and itemKey are required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Delete the progress record from the database
        userProgressRepository.deleteByUserIdAndItemTypeAndItemKey(loggedUser.getId(), itemType, itemKey);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Item marked as incomplete.");
        return ResponseEntity.ok(response);
    }

    /**
     * Fetch a list of all item keys completed by the user.
     * Useful for checking/unchecking checkboxes in the frontend.
     */
    @GetMapping("/completed-items")
    public ResponseEntity<?> getCompletedItems(HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unauthorized.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        List<UserProgress> progressList = userProgressRepository.findByUserId(loggedUser.getId());
        return ResponseEntity.ok(progressList);
    }

    /**
     * Calculate and return progress statistics for the dashboard.
     * Formulas:
     * - Roadmap Completion % = (Completed Topics / Total Topics for selected company) * 100
     * - Aptitude Tests Completed = size of testResult list
     * - Coding Questions Solved = number of coding items completed
     * - Average Score = sum(percentages) / total tests
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats(HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unauthorized.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Reload fresh user info to check selected company
        Optional<User> userOpt = userRepository.findById(loggedUser.getId());
        if (!userOpt.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        User user = userOpt.get();

        Long companyId = user.getSelectedCompanyId();

        // 1. Calculate Roadmap Completion %
        int totalCompanyTopics = 0;
        int completedCompanyTopics = 0;
        double roadmapCompletion = 0.0;

        if (companyId != null) {
            // Find all topics associated with the selected company
            List<CompanyTopic> topics = companyTopicRepository.findByCompanyId(companyId);
            totalCompanyTopics = topics.size();

            // Find all completed items for this user
            List<UserProgress> userProgress = userProgressRepository.findByUserId(user.getId());

            for (CompanyTopic topic : topics) {
                for (UserProgress progress : userProgress) {
                    if (progress.getItemType().equals("TOPIC") && 
                        progress.getItemKey().equalsIgnoreCase(topic.getTopicName())) {
                        completedCompanyTopics++;
                        break;
                    }
                }
            }

            if (totalCompanyTopics > 0) {
                roadmapCompletion = ((double) completedCompanyTopics / totalCompanyTopics) * 100.0;
                roadmapCompletion = Math.round(roadmapCompletion * 100.0) / 100.0; // round to 2 decimal places
            }
        }

        // 2. Fetch Coding Questions Solved
        List<UserProgress> progressList = userProgressRepository.findByUserId(user.getId());
        int codingQuestionsSolved = 0;
        for (UserProgress progress : progressList) {
            if (progress.getItemType().equals("CODING")) {
                codingQuestionsSolved++;
            }
        }

        // 3. Fetch Aptitude Tests Completed and Average Score
        List<TestResult> testResults = testResultRepository.findByUserIdOrderByCompletedAtDesc(user.getId());
        int aptitudeTestsCompleted = testResults.size();
        double averageScore = 0.0;

        if (aptitudeTestsCompleted > 0) {
            double totalScore = 0.0;
            for (TestResult result : testResults) {
                totalScore += result.getPercentage();
            }
            averageScore = totalScore / aptitudeTestsCompleted;
            averageScore = Math.round(averageScore * 100.0) / 100.0;
        }

        // Pack values inside a map and return
        Map<String, Object> stats = new HashMap<>();
        stats.put("roadmapCompletion", roadmapCompletion);
        stats.put("completedTopicsCount", completedCompanyTopics);
        stats.put("totalTopicsCount", totalCompanyTopics);
        stats.put("codingQuestionsSolved", codingQuestionsSolved);
        stats.put("aptitudeTestsCompleted", aptitudeTestsCompleted);
        stats.put("averageScore", averageScore);
        stats.put("selectedCompanyId", companyId);

        return ResponseEntity.ok(stats);
    }
}

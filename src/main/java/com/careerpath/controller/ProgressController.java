package com.careerpath.controller;

import com.careerpath.entity.*;
import com.careerpath.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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

    @Autowired
    private RoadmapRepository roadmapRepository;

    
    @PostMapping("/complete")
    public ResponseEntity<?> completeItem(@RequestBody Map<String, String> payload, HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unauthorized. Please log in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String itemType = payload.get("itemType"); 
        String itemKey = payload.get("itemKey");   

        if (itemType == null || itemKey == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "itemType and itemKey are required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<UserProgress> existing = userProgressRepository.findByUserIdAndItemTypeAndItemKey(
                loggedUser.getId(), itemType, itemKey);

        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get()); 
        }

        UserProgress progress = new UserProgress(loggedUser.getId(), itemType, itemKey);
        UserProgress saved = userProgressRepository.save(progress);

        return ResponseEntity.ok(saved);
    }

    
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

        
        userProgressRepository.deleteByUserIdAndItemTypeAndItemKey(loggedUser.getId(), itemType, itemKey);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Item marked as incomplete.");
        return ResponseEntity.ok(response);
    }

    
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

    
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats(HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unauthorized.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Optional<User> userOpt = userRepository.findById(loggedUser.getId());
        if (!userOpt.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        User user = userOpt.get();

        Long companyId = user.getSelectedCompanyId();

        int totalCompanyTopics = 0;
        int completedCompanyTopics = 0;
        double roadmapCompletion = 0.0;

        if (companyId != null) {
            List<Roadmap> roadmaps = roadmapRepository.findByCompanyIdOrderByWeekNumberAsc(companyId);
            List<UserProgress> userProgress = userProgressRepository.findByUserId(user.getId());

            Set<String> completedKeys = new HashSet<>();
            for (UserProgress p : userProgress) {
                if ("TOPIC".equalsIgnoreCase(p.getItemType()) && p.getItemKey() != null) {
                    completedKeys.add(p.getItemKey().trim().toLowerCase());
                }
            }

            Map<String, List<String>> structuredSections = null;
            if (companyId == 1L) {
                // TCS: 7 main sections with sub-topics for sections 1, 2, 3
                structuredSections = new LinkedHashMap<>();
                structuredSections.put("Quantitative Aptitude", Arrays.asList(
                    "Percentages, Profit & Loss",
                    "Ratio, Proportion & Averages",
                    "Time & Work, Time & Distance",
                    "Number System & Data Interpretation"
                ));
                structuredSections.put("Verbal Ability", Arrays.asList(
                    "Error Spotting & Sentence Correction",
                    "Reading Comprehension",
                    "Sentence Completion & Para Jumbles"
                ));
                structuredSections.put("Logical Reasoning", Arrays.asList(
                    "Number & Letter Series",
                    "Coding-Decoding",
                    "Blood Relations & Seating Arrangements"
                ));
                structuredSections.put("Coding Practice", Collections.emptyList());
                structuredSections.put("Programming Language", Collections.emptyList());
                structuredSections.put("Resume & Project Preparation", Collections.emptyList());
                structuredSections.put("HR Interview Preparation", Collections.emptyList());
            } else if (companyId == 2L) {
                // Infosys: 7 main sections with sub-topics for sections 1, 2, 3
                structuredSections = new LinkedHashMap<>();
                structuredSections.put("Quantitative Aptitude", Arrays.asList(
                    "Percentages, Profit & Loss, Mixtures & Allegations",
                    "Permutations & Combinations, Probability",
                    "Time & Work, Time, Speed & Distance",
                    "Number System & Data Interpretation"
                ));
                structuredSections.put("Verbal Ability", Arrays.asList(
                    "Error Spotting & Sentence Correction",
                    "Reading Comprehension",
                    "Sentence Completion & Para Jumbles"
                ));
                structuredSections.put("Logical Reasoning", Arrays.asList(
                    "Seating Arrangements & Analytical Puzzles",
                    "Syllogisms & Data Sufficiency",
                    "Coding-Decoding & Number Series"
                ));
                structuredSections.put("Coding Practice", Collections.emptyList());
                structuredSections.put("Programming Language", Collections.emptyList());
                structuredSections.put("Resume & Project Preparation", Collections.emptyList());
                structuredSections.put("HR Interview Preparation", Collections.emptyList());
            } else if (companyId == 3L) {
                // Cognizant: 7 main sections with sub-topics for sections 1, 2, 3
                structuredSections = new LinkedHashMap<>();
                structuredSections.put("Quantitative Aptitude", Arrays.asList(
                    "Percentages, Profit & Loss",
                    "Ratio, Proportion & Averages",
                    "Time & Work, Time, Speed & Distance",
                    "Number System & Data Interpretation"
                ));
                structuredSections.put("Verbal Ability & Communication", Arrays.asList(
                    "Error Spotting & Sentence Correction",
                    "Reading Comprehension",
                    "Sentence Completion & Para Jumbles"
                ));
                structuredSections.put("Logical Reasoning", Arrays.asList(
                    "Number & Letter Series",
                    "Seating Arrangements & Puzzles",
                    "Coding-Decoding & Syllogisms"
                ));
                structuredSections.put("Coding Practice", Collections.emptyList());
                structuredSections.put("Programming Language", Collections.emptyList());
                structuredSections.put("Resume & Project Preparation", Collections.emptyList());
                structuredSections.put("HR Interview Preparation", Collections.emptyList());
            } else if (companyId == 4L) {
                // Accenture: 7 main sections with sub-topics for sections 1, 2, 3
                structuredSections = new LinkedHashMap<>();
                structuredSections.put("Quantitative Aptitude", Arrays.asList(
                    "Percentages, Profit & Loss",
                    "Ratio, Proportion & Averages",
                    "Time & Work, Time, Speed & Distance",
                    "Numerical Ability & Data Interpretation"
                ));
                structuredSections.put("Verbal Ability", Arrays.asList(
                    "Error Spotting & Sentence Correction",
                    "Reading Comprehension",
                    "Vocabulary & Sentence Completion"
                ));
                structuredSections.put("Logical Reasoning", Arrays.asList(
                    "Critical Thinking & Analytical Reasoning",
                    "Abstract Reasoning & Pattern Recognition",
                    "Logical Puzzles & Problem-Solving"
                ));
                structuredSections.put("Coding Practice", Collections.emptyList());
                structuredSections.put("Programming Language", Collections.emptyList());
                structuredSections.put("Resume & Project Preparation", Collections.emptyList());
                structuredSections.put("HR Interview Preparation", Collections.emptyList());
            } else if (companyId == 5L) {
                // Capgemini: 7 main sections with sub-topics for sections 1, 2, 3
                structuredSections = new LinkedHashMap<>();
                structuredSections.put("Quantitative Aptitude", Arrays.asList(
                    "Percentages, Profit & Loss",
                    "Ratio, Proportion & Averages",
                    "Time & Work, Time, Speed & Distance",
                    "Number System & Data Interpretation"
                ));
                structuredSections.put("Verbal Ability", Arrays.asList(
                    "Error Spotting & Sentence Correction",
                    "Reading Comprehension",
                    "Vocabulary, Synonyms & Antonyms"
                ));
                structuredSections.put("Logical Reasoning", Arrays.asList(
                    "Pattern Recognition & Number Series",
                    "Seating Arrangements & Logical Puzzles",
                    "Deductive & Inductive Reasoning"
                ));
                structuredSections.put("Coding Practice", Collections.emptyList());
                structuredSections.put("Programming Language", Collections.emptyList());
                structuredSections.put("Resume & Project Preparation", Collections.emptyList());
                structuredSections.put("HR Interview Preparation", Collections.emptyList());
            }

            if (structuredSections != null) {
                totalCompanyTopics = 7;
                double totalScore = 0.0;
                int fullyCompleted = 0;

                for (Map.Entry<String, List<String>> entry : structuredSections.entrySet()) {
                    String section = entry.getKey();
                    List<String> subs = entry.getValue();

                    boolean sectionDone = completedKeys.contains(section.toLowerCase());

                    if (subs.isEmpty()) {
                        if (sectionDone) {
                            totalScore += 1.0;
                            fullyCompleted++;
                        }
                    } else {
                        int subsCount = 0;
                        for (String sub : subs) {
                            if (completedKeys.contains(sub.toLowerCase())) {
                                subsCount++;
                            }
                        }
                        if (sectionDone || subsCount == subs.size()) {
                            totalScore += 1.0;
                            fullyCompleted++;
                        } else if (subsCount > 0) {
                            totalScore += (double) subsCount / subs.size();
                        }
                    }
                }

                completedCompanyTopics = fullyCompleted;
                roadmapCompletion = Math.min(100.0, Math.round((totalScore / 7.0) * 1000.0) / 10.0);
            } else {
                totalCompanyTopics = roadmaps.size();
                for (Roadmap r : roadmaps) {
                    if (completedKeys.contains(r.getTopicName().toLowerCase())) {
                        completedCompanyTopics++;
                    }
                }
                if (totalCompanyTopics > 0) {
                    roadmapCompletion = Math.min(100.0, Math.round(((double) completedCompanyTopics / totalCompanyTopics) * 1000.0) / 10.0);
                }
            }
        }

        List<UserProgress> progressList = userProgressRepository.findByUserId(user.getId());
        int codingQuestionsSolved = 0;
        for (UserProgress progress : progressList) {
            if (progress.getItemType().equals("CODING")) {
                codingQuestionsSolved++;
            }
        }

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

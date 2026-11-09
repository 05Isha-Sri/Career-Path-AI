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
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyTopicRepository companyTopicRepository;

    @Autowired
    private RoadmapRepository roadmapRepository;

    @Autowired
    private AptitudeQuestionRepository aptitudeQuestionRepository;

    @Autowired
    private CodingQuestionRepository codingQuestionRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    
    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    
    @PostMapping("/companies")
    public ResponseEntity<?> addCompany(@RequestBody Company company, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", "Access Denied. Admin role required."));
        }
        if (company.getName() == null || company.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Company name is required."));
        }
        
        Company saved = companyRepository.save(company);
        return ResponseEntity.ok(saved);
    }

    
    @PostMapping("/company-topics")
    public ResponseEntity<?> addCompanyTopic(@RequestBody CompanyTopic topic, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", "Access Denied. Admin role required."));
        }
        if (topic.getCompanyId() == null || topic.getTopicName() == null || topic.getCategory() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "companyId, topicName, and category are required."));
        }

        if (!companyRepository.existsById(topic.getCompanyId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Company does not exist."));
        }

        CompanyTopic saved = companyTopicRepository.save(topic);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/roadmaps")
    public ResponseEntity<?> addRoadmapItem(@RequestBody Roadmap roadmap, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", "Access Denied. Admin role required."));
        }
        if (roadmap.getCompanyId() == null || roadmap.getWeekNumber() == null || roadmap.getTopicName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "companyId, weekNumber, and topicName are required."));
        }

        if (!companyRepository.existsById(roadmap.getCompanyId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Company does not exist."));
        }

        Roadmap saved = roadmapRepository.save(roadmap);
        return ResponseEntity.ok(saved);
    }

    
    @PostMapping("/aptitude")
    public ResponseEntity<?> addAptitudeQuestion(@RequestBody AptitudeQuestion question, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", "Access Denied. Admin role required."));
        }
        if (question.getQuestionText() == null || question.getCorrectOption() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Question text and correct option are required."));
        }

        AptitudeQuestion saved = aptitudeQuestionRepository.save(question);
        return ResponseEntity.ok(saved);
    }

    
    @PostMapping("/coding")
    public ResponseEntity<?> addCodingQuestion(@RequestBody CodingQuestion question, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", "Access Denied. Admin role required."));
        }
        if (question.getTitle() == null || question.getProblemStatement() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Title and problem statement are required."));
        }

        CodingQuestion saved = codingQuestionRepository.save(question);
        return ResponseEntity.ok(saved);
    }

    
    @PostMapping("/resources")
    public ResponseEntity<?> addResource(@RequestBody Resource resource, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", "Access Denied. Admin role required."));
        }
        if (resource.getTopicName() == null || resource.getTitle() == null || resource.getUrl() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "topicName, title, and url are required."));
        }

        Resource saved = resourceRepository.save(resource);
        return ResponseEntity.ok(saved);
    }

    
    @GetMapping("/stats")
    public ResponseEntity<?> getAdminStats(HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", "Access Denied. Admin role required."));
        }

        
        List<User> allUsers = userRepository.findAll();
        List<Map<String, Object>> studentList = new ArrayList<>();

        int totalStudents = 0;
        int totalAdmins = 0;

        for (User u : allUsers) {
            if ("ADMIN".equalsIgnoreCase(u.getRole())) {
                totalAdmins++;
            } else {
                totalStudents++;
                
                List<TestResult> userTests = testResultRepository.findByUserIdOrderByCompletedAtDesc(u.getId());
                
                
                Map<String, Object> uData = new HashMap<>();
                uData.put("id", u.getId());
                uData.put("username", u.getUsername());
                uData.put("email", u.getEmail());
                uData.put("testsCompleted", userTests.size());
                
                
                double avg = 0.0;
                if (!userTests.isEmpty()) {
                    double sum = 0.0;
                    for (TestResult r : userTests) {
                        sum += r.getPercentage();
                    }
                    avg = sum / userTests.size();
                    avg = Math.round(avg * 100.0) / 100.0;
                }
                uData.put("averageScore", avg);
                studentList.add(uData);
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", totalStudents);
        stats.put("totalAdmins", totalAdmins);
        stats.put("totalTestsTaken", testResultRepository.count());
        stats.put("totalCompanies", companyRepository.count());
        stats.put("totalAptitudeQuestions", aptitudeQuestionRepository.count());
        stats.put("totalCodingQuestions", codingQuestionRepository.count());
        stats.put("students", studentList);

        return ResponseEntity.ok(stats);
    }
}

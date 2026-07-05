package com.careerpath.controller;

import com.careerpath.entity.Company;
import com.careerpath.entity.CompanyTopic;
import com.careerpath.entity.Roadmap;
import com.careerpath.entity.User;
import com.careerpath.repository.CompanyRepository;
import com.careerpath.repository.CompanyTopicRepository;
import com.careerpath.repository.RoadmapRepository;
import com.careerpath.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller managing Company details, Selection, Topics, and Roadmaps.
 */
@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyTopicRepository companyTopicRepository;

    @Autowired
    private RoadmapRepository roadmapRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieve all companies.
     */
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        // Find and return all companies in the MySQL database
        return ResponseEntity.ok(companyRepository.findAll());
    }

    /**
     * Retrieve details of a specific company by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) {
            return ResponseEntity.ok(companyOpt.get());
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("error", "Company not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Fetch the topics (Aptitude, Coding, Interview) for a company.
     */
    @GetMapping("/{id}/topics")
    public ResponseEntity<List<CompanyTopic>> getCompanyTopics(@PathVariable Long id) {
        // Retrieve topics belonging to this company id
        List<CompanyTopic> topics = companyTopicRepository.findByCompanyId(id);
        return ResponseEntity.ok(topics);
    }

    /**
     * Fetch the weekly roadmap steps for a company.
     */
    @GetMapping("/{id}/roadmap")
    public ResponseEntity<List<Roadmap>> getCompanyRoadmap(@PathVariable Long id) {
        // Retrieve roadmap steps ordered sequentially by week number
        List<Roadmap> roadmap = roadmapRepository.findByCompanyIdOrderByWeekNumberAsc(id);
        return ResponseEntity.ok(roadmap);
    }

    /**
     * Set/Select the company the student is preparing for.
     * Updates the user profile in the database and active session.
     */
    @PostMapping("/select")
    public ResponseEntity<?> selectCompany(@RequestBody Map<String, Long> payload, HttpSession session) {
        // Validate if user is logged in
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Not authenticated. Please log in first.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Long companyId = payload.get("companyId");
        if (companyId == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Company ID is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Verify if company exists in the database
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (!companyOpt.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid Company ID. Company does not exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Reload fresh user data, update selection, and save
        Optional<User> userOpt = userRepository.findById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setSelectedCompanyId(companyId);
            User updatedUser = userRepository.save(user);

            // Update user details inside the HTTP Session cache
            session.setAttribute("user", updatedUser);
            return ResponseEntity.ok(updatedUser);
        }

        Map<String, String> response = new HashMap<>();
        response.put("error", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

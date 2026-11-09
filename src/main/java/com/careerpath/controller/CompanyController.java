package com.careerpath.controller;

import com.careerpath.entity.Company;
import com.careerpath.entity.CompanyTopic;
import com.careerpath.entity.Roadmap;
import com.careerpath.entity.User;
import com.careerpath.repository.CompanyRepository;
import com.careerpath.repository.CompanyTopicRepository;
import com.careerpath.repository.RoadmapRepository;
import com.careerpath.repository.UserRepository;
import com.careerpath.repository.UserProgressRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


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

    @Autowired
    private UserProgressRepository userProgressRepository;

    
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyRepository.findAll());
    }

    
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

    
    @GetMapping("/{id}/topics")
    public ResponseEntity<List<CompanyTopic>> getCompanyTopics(@PathVariable Long id) {
        
        List<CompanyTopic> topics = companyTopicRepository.findByCompanyId(id);
        return ResponseEntity.ok(topics);
    }

    
    @GetMapping("/{id}/roadmap")
    public ResponseEntity<List<Roadmap>> getCompanyRoadmap(@PathVariable Long id) {
        
        List<Roadmap> roadmap = roadmapRepository.findByCompanyIdOrderByWeekNumberAsc(id);
        return ResponseEntity.ok(roadmap);
    }

    
    @PostMapping("/select")
    public ResponseEntity<?> selectCompany(@RequestBody Map<String, Long> payload, HttpSession session) {
        
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

        
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (!companyOpt.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid Company ID. Company does not exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        
        Optional<User> userOpt = userRepository.findById(loggedUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Long previousCompanyId = user.getSelectedCompanyId();

            // If the user changes target company, clear the roadmap progress marked for the earlier company
            if (previousCompanyId == null || !previousCompanyId.equals(companyId)) {
                userProgressRepository.deleteByUserIdAndItemType(user.getId(), "TOPIC");
            }

            user.setSelectedCompanyId(companyId);
            User updatedUser = userRepository.save(user);

            
            session.setAttribute("user", updatedUser);
            return ResponseEntity.ok(updatedUser);
        }

        Map<String, String> response = new HashMap<>();
        response.put("error", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

package com.careerpath.controller;

import com.careerpath.entity.User;
import com.careerpath.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        
        if (userRepository.existsByUsername(user.getUsername())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Username is already taken!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        
        if (userRepository.existsByEmail(user.getEmail())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Email is already registered!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("STUDENT");
        } else {
            
            user.setRole(user.getRole().toUpperCase());
        }

        
        User savedUser = userRepository.save(user);
        
        return ResponseEntity.ok(savedUser);
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String identifier = credentials.get("username");
        if (identifier == null || identifier.trim().isEmpty()) {
            identifier = credentials.get("email");
        }
        if (identifier != null) {
            identifier = identifier.trim();
        }

        String password = credentials.get("password");
        if (password != null) {
            password = password.trim();
        }

        if (identifier == null || identifier.isEmpty() || password == null || password.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Username/email and password are required!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Allow logging in with either username OR email (case-insensitive)
        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(identifier);
        if (!userOpt.isPresent()) {
            userOpt = userRepository.findByEmailIgnoreCase(identifier);
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword() != null && user.getPassword().trim().equals(password)) {
                session.setAttribute("user", user);

                Map<String, Object> response = new HashMap<>();
                response.put("id", user.getId());
                response.put("username", user.getUsername());
                response.put("email", user.getEmail());
                response.put("role", user.getRole());
                response.put("selectedCompanyId", user.getSelectedCompanyId());
                return ResponseEntity.ok(response);
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid username or password!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully!");
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Optional<User> freshUserOpt = userRepository.findById(user.getId());
        if (freshUserOpt.isPresent()) {
            User freshUser = freshUserOpt.get();
            
            session.setAttribute("user", freshUser);
            return ResponseEntity.ok(freshUser);
        }

        return ResponseEntity.ok(user);
    }
}

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

/**
 * Controller handling user authentication (register, login, logout, profile).
 * Accessible via REST API /api/auth/*.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Inject UserRepository to perform database checks and operations
    @Autowired
    private UserRepository userRepository;

    /**
     * Handle user registration.
     * Receives a User object in the request body.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Validation 1: Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Username is already taken!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Validation 2: Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Email is already registered!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Set default role as STUDENT if not provided
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("STUDENT");
        } else {
            // Capitalize role for consistency
            user.setRole(user.getRole().toUpperCase());
        }

        // Save new user details in the MySQL database
        User savedUser = userRepository.save(user);
        
        // Return success response containing the saved user details
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Handle user login.
     * Takes a Map containing 'username' and 'password' keys.
     * Uses HttpSession to manage the logged-in session.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Query database to find a user with the matching username
        Optional<User> userOpt = userRepository.findByUsername(username);

        // Verify if user exists and password matches
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();
            
            // Store the logged-in User object in the Servlet HTTP Session
            session.setAttribute("user", user);

            // Return success response with user details (excluding password for security)
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("selectedCompanyId", user.getSelectedCompanyId());
            return ResponseEntity.ok(response);
        }

        // Return error if authentication fails
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid username or password!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Log out the current user.
     * Invalidates the HTTP Session.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        // Invalidate (destroy) the current HTTP session
        session.invalidate();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully!");
        return ResponseEntity.ok(response);
    }

    /**
     * Get details of the currently logged-in user.
     * Read from the active session.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        // Retrieve the 'user' object stored in the session
        User user = (User) session.getAttribute("user");

        // If no user is logged in, return 401 Unauthorized
        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // To make sure we return fresh data from the database, let's reload the user by id
        Optional<User> freshUserOpt = userRepository.findById(user.getId());
        if (freshUserOpt.isPresent()) {
            User freshUser = freshUserOpt.get();
            // Update session cache
            session.setAttribute("user", freshUser);
            return ResponseEntity.ok(freshUser);
        }

        return ResponseEntity.ok(user);
    }
}

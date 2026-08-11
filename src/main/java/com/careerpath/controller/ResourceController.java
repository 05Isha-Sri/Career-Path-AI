package com.careerpath.controller;

import com.careerpath.entity.Resource;
import com.careerpath.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller to expose study resources to students.
 * Exposes GET /api/resources (optionally filtered by topic query parameter).
 */
@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * Fetch study resources, optionally filtered by topic name (e.g. 'OOP', 'DBMS').
     */
    @GetMapping
    public ResponseEntity<List<Resource>> getResources(@RequestParam(required = false) String topic) {
        if (topic != null && !topic.trim().isEmpty() && !topic.equalsIgnoreCase("ALL")) {
            return ResponseEntity.ok(resourceRepository.findByTopicName(topic));
        }
        return ResponseEntity.ok(resourceRepository.findAll());
    }
}

package com.careerpath.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a Company (TCS, Wipro, etc.).
 * Maps to the 'companies' table.
 */
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Default No-Argument Constructor (Required by JPA)
    public Company() {
    }

    public Company(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // ===================================================================
    // Getter and Setter Methods
    // ===================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

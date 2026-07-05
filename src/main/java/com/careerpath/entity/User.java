package com.careerpath.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a User (Student or Admin) in the database.
 * Map to the 'users' table.
 */
@Entity
@Table(name = "users")
public class User {

    // Primary Key of the table, auto-incremented by the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String role; // 'STUDENT' or 'ADMIN'

    // We store company ID directly as a Long instead of @ManyToOne
    // to keep the code simple, database-independent, and easy to explain.
    @Column(name = "selected_company_id")
    private Long selectedCompanyId;

    // Default No-Argument Constructor (Required by JPA/Hibernate)
    public User() {
    }

    // Constructor with fields (except ID since it's auto-generated)
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // ===================================================================
    // Getter and Setter Methods (Standard Java Beans pattern)
    // ===================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Long selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }
}

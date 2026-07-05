package com.careerpath.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a structured weekly prep plan for a company.
 * Maps to the 'roadmaps' table.
 */
@Entity
@Table(name = "roadmaps")
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @Column(name = "topic_name", nullable = false)
    private String topicName;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Default No-Argument Constructor
    public Roadmap() {
    }

    public Roadmap(Long companyId, Integer weekNumber, String topicName, String description) {
        this.companyId = companyId;
        this.weekNumber = weekNumber;
        this.topicName = topicName;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

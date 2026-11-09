package com.careerpath.entity;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private Integer score;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Double percentage;

    @Column(name = "completed_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    
    public TestResult() {
        this.completedAt = new Date(); 
    }

    public TestResult(Long userId, Integer score, Integer totalQuestions, Double percentage) {
        this.userId = userId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.completedAt = new Date();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
}

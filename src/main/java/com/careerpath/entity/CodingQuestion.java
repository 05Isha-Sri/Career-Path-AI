package com.careerpath.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a coding practice problem.
 * Maps to the 'coding_questions' table.
 */
@Entity
@Table(name = "coding_questions")
public class CodingQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String difficulty; // 'BEGINNER', 'INTERMEDIATE', 'ADVANCED'

    @Column(name = "problem_statement", columnDefinition = "TEXT", nullable = false)
    private String problemStatement;

    @Column(name = "sample_input", columnDefinition = "TEXT")
    private String sampleInput;

    @Column(name = "sample_output", columnDefinition = "TEXT")
    private String sampleOutput;

    @Column(name = "solution_code", columnDefinition = "TEXT")
    private String solutionCode;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    // Default No-Argument Constructor
    public CodingQuestion() {
    }

    public CodingQuestion(String title, String difficulty, String problemStatement, 
                          String sampleInput, String sampleOutput, String solutionCode, String explanation) {
        this.title = title;
        this.difficulty = difficulty;
        this.problemStatement = problemStatement;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
        this.solutionCode = solutionCode;
        this.explanation = explanation;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getProblemStatement() {
        return problemStatement;
    }

    public void setProblemStatement(String problemStatement) {
        this.problemStatement = problemStatement;
    }

    public String getSampleInput() {
        return sampleInput;
    }

    public void setSampleInput(String sampleInput) {
        this.sampleInput = sampleInput;
    }

    public String getSampleOutput() {
        return sampleOutput;
    }

    public void setSampleOutput(String sampleOutput) {
        this.sampleOutput = sampleOutput;
    }

    public String getSolutionCode() {
        return solutionCode;
    }

    public void setSolutionCode(String solutionCode) {
        this.solutionCode = solutionCode;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}

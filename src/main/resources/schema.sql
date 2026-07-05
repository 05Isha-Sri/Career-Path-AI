-- ===================================================================
-- CareerPath AI Database Schema
-- This file defines the tables for the MySQL database.
-- ===================================================================

-- Table 1: Companies
-- Stores company details like TCS, Wipro, Amazon, etc.
CREATE TABLE IF NOT EXISTS companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

-- Table 2: Company Topics
-- Lists which topics (OOP, DBMS, Percentages) are expected for each company.
CREATE TABLE IF NOT EXISTS company_topics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT,
    category VARCHAR(20) NOT NULL, -- 'APTITUDE', 'CODING', 'INTERVIEW'
    topic_name VARCHAR(100) NOT NULL,
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE
);

-- Table 3: Roadmaps
-- Stores weekly tasks/roadmaps for different companies.
CREATE TABLE IF NOT EXISTS roadmaps (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT,
    week_number INT NOT NULL,
    topic_name VARCHAR(100) NOT NULL,
    description TEXT,
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE
);

-- Table 4: Users (Students and Admins)
-- Stores login and registration information.
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL, -- Stored as simple string for simplicity
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL, -- 'STUDENT' or 'ADMIN'
    selected_company_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (selected_company_id) REFERENCES companies(id) ON DELETE SET NULL
);

-- Table 5: Aptitude Questions
-- Multiple Choice Questions for Practice and Mock Tests.
CREATE TABLE IF NOT EXISTS aptitude_questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50) NOT NULL, -- 'QUANTITATIVE', 'LOGICAL', 'VERBAL'
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option VARCHAR(1) NOT NULL, -- 'A', 'B', 'C', 'D'
    explanation TEXT
);

-- Table 6: Coding Questions
-- Problem statements, sample inputs, and solutions.
CREATE TABLE IF NOT EXISTS coding_questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) UNIQUE NOT NULL,
    difficulty VARCHAR(20) NOT NULL, -- 'BEGINNER', 'INTERMEDIATE', 'ADVANCED'
    problem_statement TEXT NOT NULL,
    sample_input TEXT,
    sample_output TEXT,
    solution_code TEXT,
    explanation TEXT
);

-- Table 7: Resources
-- Reference study guides, notes, and video recommendations.
CREATE TABLE IF NOT EXISTS resources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic_name VARCHAR(100) NOT NULL,
    title VARCHAR(150) NOT NULL,
    resource_type VARCHAR(20) NOT NULL, -- 'NOTES', 'VIDEO', 'QUESTIONS'
    url VARCHAR(255) NOT NULL
);

-- Table 8: Test Results
-- Logs user mock test outcomes.
CREATE TABLE IF NOT EXISTS test_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    percentage DOUBLE NOT NULL,
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Table 9: User Progress
-- Tracks completed topics and solved coding questions.
CREATE TABLE IF NOT EXISTS user_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    item_type VARCHAR(20) NOT NULL, -- 'TOPIC' or 'CODING'
    item_key VARCHAR(100) NOT NULL, -- The topic name or coding question ID
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

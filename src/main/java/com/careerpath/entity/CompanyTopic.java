package com.careerpath.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "company_topics")
public class CompanyTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(nullable = false)
    private String category; 

    @Column(name = "topic_name", nullable = false)
    private String topicName;

    
    public CompanyTopic() {
    }

    public CompanyTopic(Long companyId, String category, String topicName) {
        this.companyId = companyId;
        this.category = category;
        this.topicName = topicName;
    }


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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}

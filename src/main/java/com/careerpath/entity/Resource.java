package com.careerpath.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic_name", nullable = false)
    private String topicName; 

    @Column(nullable = false)
    private String title;

    @Column(name = "resource_type", nullable = false)
    private String resourceType; 

    @Column(nullable = false)
    private String url;

    public Resource() {
    }

    public Resource(String topicName, String title, String resourceType, String url) {
        this.topicName = topicName;
        this.title = title;
        this.resourceType = resourceType;
        this.url = url;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

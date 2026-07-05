package com.careerpath.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Entity class representing user progress (marking topics or coding questions as completed).
 * Maps to the 'user_progress' table.
 */
@Entity
@Table(name = "user_progress")
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "item_type", nullable = false)
    private String itemType; // 'TOPIC' or 'CODING'

    @Column(name = "item_key", nullable = false)
    private String itemKey; // Holds the topic name (e.g. 'OOP') or coding question ID as a string

    @Column(name = "completed_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    // Default No-Argument Constructor
    public UserProgress() {
        this.completedAt = new Date();
    }

    public UserProgress(Long userId, String itemType, String itemKey) {
        this.userId = userId;
        this.itemType = itemType;
        this.itemKey = itemKey;
        this.completedAt = new Date();
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
}

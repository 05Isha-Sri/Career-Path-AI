package com.careerpath.repository;

import com.careerpath.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for database operations on UserProgress entity.
 */
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    // Fetch all progress records for a user
    List<UserProgress> findByUserId(Long userId);

    // Find progress of a specific item (e.g. check if user completed a topic or a coding question)
    Optional<UserProgress> findByUserIdAndItemTypeAndItemKey(Long userId, String itemType, String itemKey);

    // Delete progress of a specific item (to mark it as incomplete/uncheck it).
    // @Transactional is required because deleting records modifies the database state.
    @Transactional
    void deleteByUserIdAndItemTypeAndItemKey(Long userId, String itemType, String itemKey);
}

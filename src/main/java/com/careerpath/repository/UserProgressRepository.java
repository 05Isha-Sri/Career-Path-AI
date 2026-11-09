package com.careerpath.repository;

import com.careerpath.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    List<UserProgress> findByUserId(Long userId);

    Optional<UserProgress> findByUserIdAndItemTypeAndItemKey(Long userId, String itemType, String itemKey);

    @Transactional
    void deleteByUserIdAndItemTypeAndItemKey(Long userId, String itemType, String itemKey);

    @Transactional
    void deleteByUserIdAndItemType(Long userId, String itemType);
}

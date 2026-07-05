package com.careerpath.repository;

import com.careerpath.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for database operations on Roadmap entity.
 */
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    // Fetch the weekly preparation steps ordered by week number
    List<Roadmap> findByCompanyIdOrderByWeekNumberAsc(Long companyId);
}

package com.careerpath.repository;

import com.careerpath.entity.CompanyTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompanyTopicRepository extends JpaRepository<CompanyTopic, Long> {

    List<CompanyTopic> findByCompanyId(Long companyId);
}

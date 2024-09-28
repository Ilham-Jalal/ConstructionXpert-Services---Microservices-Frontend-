package com.project_service.repository;

import com.project_service.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    @Query("SELECT DISTINCT CASE " +
            "WHEN LOWER(p.name) LIKE LOWER(CONCAT('%', :input, '%')) THEN p.name " +
            "WHEN LOWER(p.description) LIKE LOWER(CONCAT('%', :input, '%')) THEN p.description " +
            "WHEN LOWER(p.geolocation) LIKE LOWER(CONCAT('%', :input, '%')) THEN p.geolocation " +
            "END AS suggestion " +
            "FROM Project p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :input, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :input, '%')) " +
            "OR LOWER(p.geolocation) LIKE LOWER(CONCAT('%', :input, '%'))")
    List<String> findAutocompleteSuggestions(@Param("input") String input);
}

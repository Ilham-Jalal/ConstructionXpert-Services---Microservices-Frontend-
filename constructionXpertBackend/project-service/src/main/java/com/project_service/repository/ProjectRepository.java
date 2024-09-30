package com.project_service.repository;

import com.project_service.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    @Query("SELECT p.name FROM Project p WHERE LOWER(p.name) LIKE :input OR LOWER(p.description) LIKE :input OR LOWER(p.geolocation) LIKE :input")
    List<String> findAutocompleteSuggestions(String input);
}

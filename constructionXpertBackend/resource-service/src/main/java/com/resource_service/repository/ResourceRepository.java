package com.resource_service.repository;

import com.resource_service.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {
    @Query(value = "SELECT search FROM resource WHERE LOWER(title) LIKE LOWER(:input) OR LOWER(description) LIKE LOWER(:input) OR LOWER(provider) LIKE LOWER(:input)", nativeQuery = true)
    List<String> findAutocompleteSuggestions(@Param("input") String input);
}

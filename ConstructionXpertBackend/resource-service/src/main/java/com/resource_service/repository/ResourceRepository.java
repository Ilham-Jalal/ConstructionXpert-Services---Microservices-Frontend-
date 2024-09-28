package com.resource_service.repository;

import com.resource_service.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {
    @Query(value = "SELECT DISTINCT CASE " +
            "WHEN LOWER(title) LIKE LOWER(CONCAT('%', :input, '%')) THEN title " +
            "WHEN LOWER(provider) LIKE LOWER(CONCAT('%', :input, '%')) THEN provider " +
            "END AS suggestion " +
            "FROM resource " +
            "WHERE LOWER(title) LIKE LOWER(CONCAT('%', :input, '%')) " +
            "OR LOWER(provider) LIKE LOWER(CONCAT('%', :input, '%'))",
            nativeQuery = true)
    List<String> findAutocompleteSuggestions(@Param("input") String input);
}

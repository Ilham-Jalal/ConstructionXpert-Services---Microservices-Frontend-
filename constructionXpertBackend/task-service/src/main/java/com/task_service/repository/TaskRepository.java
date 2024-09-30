package com.task_service.repository;

import com.task_service.enums.Priority;
import com.task_service.enums.Status;
import com.task_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByProjectId(Long id);
    @Query("select t.id from Task t where t.projectId = ?1")
    List<Long> getIdsByProject(Long id);

    @Query(name = "Task.findFilteredTasks")
    List<String> findAutocompleteSuggestions(@Param("input") String input);
}

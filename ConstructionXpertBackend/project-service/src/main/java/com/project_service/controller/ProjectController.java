package com.project_service.controller;

import com.project_service.dto.ProjectDto;
import com.project_service.enums.Status;
import com.project_service.exception.ProjectNotFoundException;
import com.project_service.model.Project;
import com.project_service.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/get-all-projects")
    public ResponseEntity<?> getAllProjects(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "name", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            Page<Project> projects = projectService.getAllProjects(page, size, sortField, sortDirection);
            return ResponseEntity.ok(projects);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-project-by-id/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable("id") String id) {
        try {
            var project = projectService.getProjectById(Long.valueOf(id));
            return ResponseEntity.ok(project);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/create-project")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {
        try {
            var createdProject = projectService.createProject(projectDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-project/{id}")
    public ResponseEntity<?> updateProject(
            @PathVariable("id") String id,
            @ModelAttribute ProjectDto projectDto) {
        try {
            var updatedProject = projectService.updateProject(Long.valueOf(id), projectDto);
            return ResponseEntity.ok(updatedProject);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-project/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable("id") String id) {
        try {
            projectService.deleteProject(Long.valueOf(id));
            return ResponseEntity.noContent().build();
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/dynamic-filter")
    public ResponseEntity<?> dynamicFilterProjects(
            @RequestParam(required = false, name = "geolocation") String geolocation,
            @RequestParam(required = false, name = "status") Status status,
            @RequestParam(required = false, name = "minBudget") Double minBudget,
            @RequestParam(required = false, name = "maxBudget") Double maxBudget,
            @RequestParam(required = false, name = "dateStart") Date dateStart,
            @RequestParam(required = false, name = "dateEnd") Date dateEnd,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "name", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var projects = projectService.dynamicFilterProjects(geolocation, status, minBudget, maxBudget, dateStart, dateEnd, page, size, sortField, sortDirection);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/dynamic-search")
    public ResponseEntity<?> dynamicSearchProjects(
            @RequestParam("input") String input,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "name", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var projects = projectService.dynamicSearchProjects(input, page, size, sortField, sortDirection);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/autocomplete-search")
    public ResponseEntity<?> autocompleteSearchInput(@RequestParam("input") String input) {
        try {
            List<String> suggestions = projectService.autocompleteSearchInput(input);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

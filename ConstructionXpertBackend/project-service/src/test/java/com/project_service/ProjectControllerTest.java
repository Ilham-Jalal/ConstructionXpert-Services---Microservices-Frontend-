package com.project_service;

import com.project_service.controller.ProjectController;
import com.project_service.dto.ProjectDto;
import com.project_service.enums.Status;
import com.project_service.exception.ProjectNotFoundException;
import com.project_service.model.Project;
import com.project_service.service.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProjects() {
        List<Project> projectList = Arrays.asList(new Project(), new Project());
        Page<Project> projects = new PageImpl<>(projectList);
        when(projectService.getAllProjects(0, 10, "name", "asc")).thenReturn(projects);

        ResponseEntity<?> response = projectController.getAllProjects(0, 10, "name", "asc");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projects, response.getBody());
    }

    @Test
    public void testGetAllProjectsNotFound() {
        when(projectService.getAllProjects(0, 10, "name", "asc"))
                .thenThrow(new ProjectNotFoundException("Projects not found!"));

        ResponseEntity<?> response = projectController.getAllProjects(0, 10, "name", "asc");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Projects not found!", response.getBody());
    }

    @Test
    public void testGetProjectById() {
        Long projectId = 1L;
        ProjectDto project = new ProjectDto();
        when(projectService.getProjectById(projectId)).thenReturn(project);

        ResponseEntity<?> response = projectController.getProjectById(projectId.toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project, response.getBody());
    }

    @Test
    public void testGetProjectByIdNotFound() {
        Long projectId = 1L;
        when(projectService.getProjectById(projectId))
                .thenThrow(new ProjectNotFoundException(String.format("Project with %d not found!", projectId)));

        ResponseEntity<?> response = projectController.getProjectById(projectId.toString());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(String.format("Project with %d not found!", projectId), response.getBody());
    }

    @Test
    public void testCreateProject() {
        ProjectDto projectDto = new ProjectDto();
        ProjectDto createdProjectDto = new ProjectDto();
        when(projectService.createProject(projectDto)).thenReturn(createdProjectDto);

        ResponseEntity<?> response = projectController.createProject(projectDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdProjectDto, response.getBody());
    }

    @Test
    public void testCreateProjectException() {
        ProjectDto projectDto = new ProjectDto();
        when(projectService.createProject(projectDto))
                .thenThrow(new RuntimeException("Error creating project"));

        ResponseEntity<?> response = projectController.createProject(projectDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error creating project", response.getBody());
    }

    @Test
    public void testUpdateProject() {
        Long projectId = 1L;
        ProjectDto projectDto = new ProjectDto();
        ProjectDto updatedProjectDto = new ProjectDto();
        when(projectService.updateProject(projectId, projectDto)).thenReturn(updatedProjectDto);

        ResponseEntity<?> response = projectController.updateProject(projectId.toString(), projectDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProjectDto, response.getBody());
    }

    @Test
    public void testUpdateProjectNotFound() {
        Long projectId = 1L;
        ProjectDto projectDto = new ProjectDto();
        when(projectService.updateProject(projectId, projectDto))
                .thenThrow(new ProjectNotFoundException(String.format("Project with %d not found!", projectId)));

        ResponseEntity<?> response = projectController.updateProject(projectId.toString(), projectDto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(String.format("Project with %d not found!", projectId), response.getBody());
    }

    @Test
    public void testDeleteProject() {
        long projectId = 1L;

        ResponseEntity<?> response = projectController.deleteProject(Long.toString(projectId));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteProjectNotFound() {
        Long projectId = 1L;
        doThrow(new ProjectNotFoundException(String.format("Project with %d not found!", projectId)))
                .when(projectService).deleteProject(projectId);

        ResponseEntity<?> response = projectController.deleteProject(projectId.toString());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(String.format("Project with %d not found!", projectId), response.getBody());
    }

    @Test
    public void testDeleteProjectException() {
        Long projectId = 1L;
        doThrow(new RuntimeException("Error deleting project"))
                .when(projectService).deleteProject(projectId);

        ResponseEntity<?> response = projectController.deleteProject(projectId.toString());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error deleting project", response.getBody());
    }

    @Test
    public void testDynamicFilterProjects() {
        String geolocation = "Khénifra";
        Status status = Status.COMPLETED;
        Double minBudget = 1000.0;
        Double maxBudget = 5000.0;
        Date dateStart = Date.valueOf("2002-12-02");
        Date dateEnd = Date.valueOf("2002-12-02");
        int page = 0;
        int size = 10;
        String sortField = "name";
        String sortDirection = "asc";

        Page<Project> projects = new PageImpl<>(Arrays.asList(new Project(), new Project()));

        when(projectService.dynamicFilterProjects(
                geolocation,
                status,
                minBudget,
                maxBudget,
                dateStart,
                dateEnd,
                page,
                size,
                sortField,
                sortDirection)).thenReturn(projects);

        ResponseEntity<?> response = projectController.dynamicFilterProjects(
                geolocation,
                status,
                minBudget,
                maxBudget,
                dateStart,
                dateEnd,
                page,
                size,
                sortField,
                sortDirection);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projects, response.getBody());
    }

    @Test
    public void testDynamicFilterProjectsNotFound() {
        String geolocation = "InvalidLocation";
        Status status = Status.COMPLETED;
        Double minBudget = 1000.0;
        Double maxBudget = 5000.0;
        Date dateStart = Date.valueOf("2002-12-02");
        Date dateEnd = Date.valueOf("2002-12-02");
        int page = 0;
        int size = 10;
        String sortField = "name";
        String sortDirection = "asc";

        when(projectService.dynamicFilterProjects(
                geolocation,
                status,
                minBudget,
                maxBudget,
                dateStart,
                dateEnd,
                page,
                size,
                sortField,
                sortDirection))
                .thenThrow(new ProjectNotFoundException("No projects found with the specified filters."));

        ResponseEntity<?> response = projectController.dynamicFilterProjects(
                geolocation,
                status,
                minBudget,
                maxBudget,
                dateStart,
                dateEnd,
                page,
                size,
                sortField,
                sortDirection);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No projects found with the specified filters.", response.getBody());
    }


    @Test
    public void testDynamicSearchProjects() {
        List<Project> projectList = Arrays.asList(new Project(), new Project());
        Page<Project> searchedProjects = new PageImpl<>(projectList);
        String searchTerm = "test";
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String order = "asc";

        when(projectService.dynamicSearchProjects(searchTerm, page, size, sortBy, order)).thenReturn(searchedProjects);

        ResponseEntity<?> response = projectController.dynamicSearchProjects(searchTerm, page, size, sortBy, order);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(searchedProjects, response.getBody());
    }

    @Test
    public void testDynamicSearchProjectsNotFound() {
        String searchTerm = "invalid";
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String order = "asc";

        when(projectService.dynamicSearchProjects(searchTerm, page, size, sortBy, order))
                .thenThrow(new ProjectNotFoundException("No projects found for the given search term."));

        ResponseEntity<?> response = projectController.dynamicSearchProjects(searchTerm, page, size, sortBy, order);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No projects found for the given search term.", response.getBody());
    }

    @Test
    public void testAutocompleteSearchInput() {
        List<String> autocompleteSuggestions = Arrays.asList("Project A", "Project B");
        String input = "Pro";

        when(projectService.autocompleteSearchInput(input)).thenReturn(autocompleteSuggestions);

        ResponseEntity<?> response = projectController.autocompleteSearchInput(input);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(autocompleteSuggestions, response.getBody());
    }

    @Test
    public void testAutocompleteSearchInputNotFound() {
        String input = "Nonexistent";
        when(projectService.autocompleteSearchInput(input))
                .thenThrow(new ProjectNotFoundException("No autocomplete suggestions found."));

        ResponseEntity<?> response = projectController.autocompleteSearchInput(input);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No autocomplete suggestions found.", response.getBody());
    }
}

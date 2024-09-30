package com.project_service.service;

import com.project_service.client.RequestContext;
import com.project_service.dto.ProjectDto;
import com.project_service.enums.Status;
import com.project_service.exception.ProjectNotFoundException;
import com.project_service.mapper.ProjectMapper;
import com.project_service.model.Project;
import com.project_service.repository.ProjectRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final RestTemplate restTemplate;
    private final String TASK_SERVICE_URL = "http://task-service/api/tasks";
    private final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private HttpEntity<Void> createHttpEntity() {
        String token = RequestContext.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    public ProjectDto createProject(ProjectDto projectDto) {
        var project = projectMapper.toEntity(projectDto);
        var savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    public Page<Project> getAllProjects(int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        var projects = projectRepository.findAll(pageable);
        if (projects.getContent().isEmpty()) {
            throw new ProjectNotFoundException("Projects not found!");
        }
        return projects;
    }

    public ProjectDto getProjectById(Long id) {
        var project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(String.format("Project with %d not found!", id)));
        return projectMapper.toDto(project);
    }

    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        var project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(String.format("Project with %d not found!", id)));
        var updatedProject = projectMapper.partialUpdate(projectDto, project);
        var savedProject = projectRepository.save(updatedProject);
        return projectMapper.toDto(savedProject);
    }

    @CircuitBreaker(name = "taskServiceCircuitBreaker", fallbackMethod = "taskServiceFallback")
    public void deleteProject(Long id) {
        var project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(String.format("Project with %d not found!", id)));
        ResponseEntity<List<Long>> tasks = restTemplate.exchange(
                String.format("%s/get-tasks-ids-by-project/%d", TASK_SERVICE_URL, project.getId()),
                HttpMethod.GET,
                createHttpEntity(),
                new ParameterizedTypeReference<List<Long>>() {}
        );

        if (tasks.getStatusCode().is2xxSuccessful()) {
            Objects.requireNonNull(tasks.getBody()).forEach(task ->
                    restTemplate.exchange(
                            String.format("%s/delete-task/%d", TASK_SERVICE_URL, task),
                            HttpMethod.DELETE,
                            createHttpEntity(),
                            Void.class
                    ));
        }
        projectRepository.delete(project);
    }

    public String taskServiceFallback(Long projectId, Throwable throwable) {
        logger.error("Task Service call failed: {}", throwable.getMessage());
        return "Task service is temporarily unavailable. Please try again later.";
    }

    public Page<Project> dynamicFilterProjects(String geolocation, Status status, Double minBudget, Double maxBudget, Date dateStart, Date dateEnd, int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Specification<Project> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (geolocation != null && !geolocation.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("geolocation")), "%" + geolocation.toLowerCase() + "%"));
            }

            if (minBudget != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("budget"), minBudget));
            }

            if (maxBudget != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("budget"), maxBudget));
            }

            if (dateStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateStart"), dateStart));
            }

            if (dateEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateEnd"), dateEnd));
            }

            assert query != null;
            query.orderBy(criteriaBuilder.asc(root.get(sortField)));

            query.distinct(false);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return projectRepository.findAll(spec, pageable);
    }

    public Page<Project> dynamicSearchProjects(String input, int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Specification<Project> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (input != null && !input.isEmpty()) {
                String searchPattern = "%" + input.toLowerCase() + "%";

                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("geolocation")), searchPattern)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return projectRepository.findAll(spec, pageable);
    }

    public List<String> autocompleteSearchInput(String input) {
        if (input == null || input.isEmpty()) {
            return List.of();
        }
        return projectRepository.findAutocompleteSuggestions("%" + input.toLowerCase() + "%");
    }


//    public Page<Project> dynamicSearchProjects(String name, String geolocation, Status status, Double minBudget, Double maxBudget, int page, int size, String sortField, String sortDirection) {
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//
//        if (name != null && !name.isEmpty()) {
//            boolQuery.must(QueryBuilders.matchQuery("name", name));
//        }
//        if (geolocation != null && !geolocation.isEmpty()) {
//            boolQuery.must(QueryBuilders.matchQuery("geolocation", geolocation));
//        }
//        if (status != null) {
//            boolQuery.must(QueryBuilders.matchQuery("status", status));
//        }
//        if (minBudget != null) {
//            boolQuery.must(QueryBuilders.rangeQuery("budget").gte(minBudget));
//        }
//        if (maxBudget != null) {
//            boolQuery.must(QueryBuilders.rangeQuery("budget").lte(maxBudget));
//        }
//
//        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(boolQuery)
//                .withPageable(pageable)
//                .build();
//
//        SearchHits<Project> searchHits = elasticsearchRestTemplate.search(searchQuery, Project.class);
//        List<Project> projectList = searchHits.getSearchHits().stream()
//                .map(hit -> hit.getContent())
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(projectList, pageable, searchHits.getTotalHits());
//    }

}

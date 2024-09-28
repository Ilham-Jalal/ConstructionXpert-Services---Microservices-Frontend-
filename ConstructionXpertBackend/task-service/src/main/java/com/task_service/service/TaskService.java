package com.task_service.service;

import com.task_service.client.RequestContext;
import com.task_service.dto.TaskDto;
import com.task_service.enums.Priority;
import com.task_service.enums.Status;
import com.task_service.exception.TaskNotFoundException;
import com.task_service.mapper.TaskMapper;
import com.task_service.model.Task;
import com.task_service.repository.TaskRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final RestTemplate restTemplate;
    private final String PROJECT_SERVICE_URL = "http://project-service/api/project";
    private final String RESOURCE_SERVICE_URL = "http://resource-service/api/resources";

    private HttpEntity<Void> createHttpEntity() {
        String token = RequestContext.getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    @CircuitBreaker(name = "resourceServiceCircuitBreaker", fallbackMethod = "resourceServiceFallback")
    public TaskDto createTask(TaskDto taskDto) {
        for (Long resourceId : taskDto.getRIds()) {
            var resourceResult = restTemplate.exchange(
                    String.format("%s/get-resource-by-id/%d", RESOURCE_SERVICE_URL, resourceId),
                    HttpMethod.GET,
                    createHttpEntity(),
                    Void.class
            );
            if (resourceResult.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                throw new RuntimeException("Resource not found!");
            }
        }
        var projectResult = restTemplate.exchange(
                String.format("%s/get-project-by-id/%d", PROJECT_SERVICE_URL, taskDto.getProjectId()),
                HttpMethod.GET,
                createHttpEntity(),
                Void.class
        );
        if (projectResult.getStatusCode().is2xxSuccessful()) {
            var task = taskMapper.toEntity(taskDto);
            task.setResourceIds(taskDto.getRIds().stream().map(Objects::toString).collect(Collectors.joining(", ")));
            var savedTask = taskRepository.save(task);
            var mappedTask = taskMapper.toDto(savedTask);
            mappedTask.setRIds((ArrayList<Long>) Arrays.stream(savedTask.getResourceIds().split(", ")).map(Long::valueOf).collect(Collectors.toList()));
            return mappedTask;
        } else {
            throw new RuntimeException("Project not found!");
        }
    }

    @CircuitBreaker(name = "resourceServiceCircuitBreaker", fallbackMethod = "resourceServiceFallback")
    public TaskDto getTaskById(Long id) {
        var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(String.format("Task with %d not found!", id)));
        var mappedTask = taskMapper.toDto(task);
        if (task.getResourceIds() != null){
            mappedTask.setRIds((ArrayList<Long>) Arrays.stream(task.getResourceIds().split(", ")).map(Long::valueOf).collect(Collectors.toList()));
        }
        return mappedTask;
    }

    @CircuitBreaker(name = "projectServiceCircuitBreaker", fallbackMethod = "projectServiceFallback")
    public List<Task> getTasksByProjectId(Long projectId) {
        var projectResult = restTemplate.exchange(
                String.format("%s/get-project-by-id/%d", PROJECT_SERVICE_URL, projectId),
                HttpMethod.GET,
                createHttpEntity(),
                Void.class
        );
        if (projectResult.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            throw new RuntimeException("Project not found!");
        }
        var tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("Tasks not found!");
        }
        return tasks;
    }

    @CircuitBreaker(name = "projectServiceCircuitBreaker", fallbackMethod = "projectServiceFallback")
    public List<Long> getTasksIdsByProjectId(Long projectId) {
        var projectResult = restTemplate.exchange(
                String.format("%s/get-project-by-id/%d", PROJECT_SERVICE_URL, projectId),
                HttpMethod.GET,
                createHttpEntity(),
                Void.class
        );
        if (projectResult.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            throw new RuntimeException("Project not found!");
        }
        var tasksIds = taskRepository.getIdsByProject(projectId);
        if (tasksIds.isEmpty()) {
            throw new TaskNotFoundException("Tasks ids not found!");
        }
        return tasksIds;
    }

    @CircuitBreaker(name = "resourceServiceCircuitBreaker", fallbackMethod = "resourceServiceFallback")
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(String.format("Task with %d not found!", id)));
        for (Long resourceId : taskDto.getRIds()) {
            var resourceResult = restTemplate.exchange(
                    String.format("%s/get-resource-by-id/%d", RESOURCE_SERVICE_URL, resourceId),
                    HttpMethod.GET,
                    createHttpEntity(),
                    Void.class
            );
            if (resourceResult.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                throw new RuntimeException("Resource not found!");
            }
        }
        var projectResult = restTemplate.exchange(
                String.format("%s/get-project-by-id/%d", PROJECT_SERVICE_URL, taskDto.getProjectId()),
                HttpMethod.GET,
                createHttpEntity(),
                Void.class
        );
        if (projectResult.getStatusCode().is2xxSuccessful()) {
            var updatedTask = taskMapper.partialUpdate(taskDto, task);
            updatedTask.setResourceIds(taskDto.getRIds().stream().map(Objects::toString).collect(Collectors.joining(", ")));
            var savedTask = taskRepository.save(updatedTask);
            var mappedTask = taskMapper.toDto(savedTask);
            mappedTask.setRIds((ArrayList<Long>) Arrays.stream(savedTask.getResourceIds().split(", ")).map(Long::valueOf).collect(Collectors.toList()));
            return mappedTask;
        } else {
            throw new RuntimeException("Project not found!");
        }
    }

    public void deleteTask(Long id) {
        var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(String.format("Task with %d not found!", id)));
        taskRepository.delete(task);
    }

    public String resourceServiceFallback(Long resourceId, Throwable throwable) {
        return "Resource service is temporarily unavailable. Please try again later.";
    }

    public String projectServiceFallback(Long projectId, Throwable throwable) {
        return "Project service is temporarily unavailable. Please try again later.";
    }

    public Page<TaskDto> getAllTasks(int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        var tasks = taskRepository.findAll(pageable);
        if (tasks.getContent().isEmpty()) {
            throw new TaskNotFoundException("Tasks not found!");
        }
        return tasks.map(taskMapper::toDto);
    }

    public Page<Task> dynamicFilterTasks(String type, Date startDate, Date endDate,
                                            Priority priority, Status status,
                                            int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Specification<Task> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (type != null && !type.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
            }

            if (priority != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return taskRepository.findAll(spec, pageable);
    }

    public Page<Task> dynamicSearchTasks(String input, int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Specification<Task> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (input != null && !input.isEmpty()) {
                String searchPattern = "%" + input.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return taskRepository.findAll(spec, pageable);
    }

    public List<String> autocompleteSearchInput(String input) {
        if (input == null || input.isEmpty()) {
            return List.of();
        }
        return taskRepository.findAutocompleteSuggestions(input);
    }
}

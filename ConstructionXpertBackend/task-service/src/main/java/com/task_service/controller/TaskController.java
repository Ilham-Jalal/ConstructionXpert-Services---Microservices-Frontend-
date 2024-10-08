package com.task_service.controller;

import com.task_service.dto.TaskDto;
import com.task_service.enums.Priority;
import com.task_service.enums.Status;
import com.task_service.exception.TaskNotFoundException;
import com.task_service.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/get-all-tasks")
    public ResponseEntity<?> getAllTasks(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "title", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var tasks = taskService.getAllTasks(page, size, sortField, sortDirection);
            return ResponseEntity.ok(tasks);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-task-by-id/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable("id") Long id) {
        try {
            var task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/create-task")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
        try {
            var createdTask = taskService.createTask(taskDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-task/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable("id") Long id,
            @RequestBody TaskDto taskDto) {
        try {
            var updatedTask = taskService.updateTask(id, taskDto);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/dynamic-filter")
    public ResponseEntity<?> dynamicFilterTasks(
            @RequestParam(required = false, name = "type") String type,
            @RequestParam(required = false, name = "startDate") Date startDate,
            @RequestParam(required = false, name = "endDate") Date endDate,
            @RequestParam(required = false, name = "priority") Priority priority,
            @RequestParam(required = false, name = "status") Status status,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "title", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var tasks = taskService.dynamicFilterTasks(type, startDate, endDate, priority, status, page, size, sortField, sortDirection);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/dynamic-search")
    public ResponseEntity<?> dynamicSearchTasks(
            @RequestParam("input") String input,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "title", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var tasks = taskService.dynamicSearchTasks(input, page, size, sortField, sortDirection);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/autocomplete-search")
    public ResponseEntity<?> autocompleteSearchInput(@RequestParam("input") String input) {
        try {
            var suggestions = taskService.autocompleteSearchInput(input);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

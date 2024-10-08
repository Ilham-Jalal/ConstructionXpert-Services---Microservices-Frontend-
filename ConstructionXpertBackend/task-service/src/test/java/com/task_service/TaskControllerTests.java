package com.task_service;

import com.task_service.controller.TaskController;
import com.task_service.dto.TaskDto;
import com.task_service.exception.TaskNotFoundException;
import com.task_service.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TaskControllerTests {
    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_Success() {
        TaskDto inputDto = new TaskDto();
        TaskDto createdDto = new TaskDto();
        when(taskService.createTask(inputDto)).thenReturn(createdDto);

        ResponseEntity<?> response = taskController.createTask(inputDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdDto, response.getBody());
    }

    @Test
    void createTask_Exception() {
        TaskDto inputDto = new TaskDto();
        when(taskService.createTask(inputDto)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = taskController.createTask(inputDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getTaskById_Success() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        when(taskService.getTaskById(taskId)).thenReturn(taskDto);

        ResponseEntity<?> response = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDto, response.getBody());
    }

    @Test
    void getTaskById_NotFound() {
        Long taskId = 1L;
        when(taskService.getTaskById(taskId)).thenThrow(new TaskNotFoundException("Task not found"));

        ResponseEntity<?> response = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task not found", response.getBody());
    }

    @Test
    void updateTask_Success() {
        Long taskId = 1L;
        TaskDto inputDto = new TaskDto();
        TaskDto updatedDto = new TaskDto();
        when(taskService.updateTask(taskId, inputDto)).thenReturn(updatedDto);

        ResponseEntity<?> response = taskController.updateTask(taskId, inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDto, response.getBody());
    }

    @Test
    void updateTask_NotFound() {
        Long taskId = 1L;
        TaskDto inputDto = new TaskDto();
        when(taskService.updateTask(taskId, inputDto)).thenThrow(new TaskNotFoundException("Task not found"));

        ResponseEntity<?> response = taskController.updateTask(taskId, inputDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task not found", response.getBody());
    }

    @Test
    void updateTask_Exception() {
        Long taskId = 1L;
        TaskDto inputDto = new TaskDto();
        when(taskService.updateTask(taskId, inputDto)).thenThrow(new TaskNotFoundException(String.valueOf(taskId)));

        ResponseEntity<?> response = taskController.updateTask(taskId, inputDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new TaskNotFoundException(String.valueOf(taskId)).getMessage(), response.getBody());
    }

    @Test
    void deleteTask_Success() {
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);

        ResponseEntity<?> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteTask_NotFound() {
        Long taskId = 1L;
        doThrow(new TaskNotFoundException("Task not found")).when(taskService).deleteTask(taskId);

        ResponseEntity<?> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task not found", response.getBody());
    }
}

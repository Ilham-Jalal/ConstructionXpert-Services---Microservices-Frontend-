package com.resource_service;

import com.resource_service.controller.ResourceController;
import com.resource_service.dto.ResourceDto;
import com.resource_service.exception.ResourceNotFoundException;
import com.resource_service.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class ResourceControllerTests {

    @Mock
    private ResourceService resourceService;

    @InjectMocks
    private ResourceController resourceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllResources() {
        List<ResourceDto> resourceList = Arrays.asList(new ResourceDto(), new ResourceDto());
        Page<ResourceDto> resources = new PageImpl<>(resourceList);
        when(resourceService.getAllResources(0, 10, "name", "asc")).thenReturn(resources);

        ResponseEntity<?> response = resourceController.getAllResources(0, 10, "name", "asc");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resources, response.getBody());
    }

    @Test
    public void testGetAllResourcesNotFound() {
        when(resourceService.getAllResources(0, 10, "name", "asc"))
                .thenThrow(new ResourceNotFoundException("Resources not found!"));

        ResponseEntity<?> response = resourceController.getAllResources(0, 10, "name", "asc");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resources not found!", response.getBody());
    }

    @Test
    public void testGetResourceById() {
        Long resourceId = 1L;
        ResourceDto resource = new ResourceDto();
        when(resourceService.getResourceById(resourceId)).thenReturn(resource);

        ResponseEntity<?> response = resourceController.getResourceById(resourceId.toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resource, response.getBody());
    }

    @Test
    public void testGetResourceByIdNotFound() {
        Long resourceId = 1L;
        when(resourceService.getResourceById(resourceId))
                .thenThrow(new ResourceNotFoundException(String.format("Resource with %d not found!", resourceId)));

        ResponseEntity<?> response = resourceController.getResourceById(resourceId.toString());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(String.format("Resource with %d not found!", resourceId), response.getBody());
    }

    @Test
    public void testCreateResource() {
        ResourceDto resourceDto = new ResourceDto();
        ResourceDto createdResourceDto = new ResourceDto();
        when(resourceService.createResource(resourceDto)).thenReturn(createdResourceDto);

        ResponseEntity<?> response = resourceController.createResource(resourceDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdResourceDto, response.getBody());
    }

    @Test
    public void testCreateResourceException() {
        ResourceDto resourceDto = new ResourceDto();
        when(resourceService.createResource(resourceDto))
                .thenThrow(new RuntimeException("Error creating resource"));

        ResponseEntity<?> response = resourceController.createResource(resourceDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error creating resource", response.getBody());
    }

    @Test
    public void testUpdateResource() {
        Long resourceId = 1L;
        ResourceDto resourceDto = new ResourceDto();
        ResourceDto updatedResourceDto = new ResourceDto();
        when(resourceService.updateResource(resourceId, resourceDto)).thenReturn(updatedResourceDto);

        ResponseEntity<?> response = resourceController.updateResource(resourceId.toString(), resourceDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedResourceDto, response.getBody());
    }

    @Test
    public void testUpdateResourceNotFound() {
        Long resourceId = 1L;
        ResourceDto resourceDto = new ResourceDto();
        when(resourceService.updateResource(resourceId, resourceDto))
                .thenThrow(new ResourceNotFoundException(String.format("Resource with %d not found!", resourceId)));

        ResponseEntity<?> response = resourceController.updateResource(resourceId.toString(), resourceDto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(String.format("Resource with %d not found!", resourceId), response.getBody());
    }

    @Test
    public void testDeleteResource() {
        long resourceId = 1L;

        ResponseEntity<?> response = resourceController.deleteResource(Long.toString(resourceId));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteResourceNotFound() {
        Long resourceId = 1L;
        doThrow(new ResourceNotFoundException(String.format("Resource with %d not found!", resourceId)))
                .when(resourceService).deleteResource(resourceId);

        ResponseEntity<?> response = resourceController.deleteResource(resourceId.toString());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(String.format("Resource with %d not found!", resourceId), response.getBody());
    }

    @Test
    public void testDeleteResourceException() {
        Long resourceId = 1L;
        doThrow(new RuntimeException("Error deleting resource"))
                .when(resourceService).deleteResource(resourceId);

        ResponseEntity<?> response = resourceController.deleteResource(resourceId.toString());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error deleting resource", response.getBody());
    }
}

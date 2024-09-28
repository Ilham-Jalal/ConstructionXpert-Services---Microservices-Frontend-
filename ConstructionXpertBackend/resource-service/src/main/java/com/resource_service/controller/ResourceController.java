package com.resource_service.controller;

import com.resource_service.dto.ResourceDto;
import com.resource_service.enums.Type;
import com.resource_service.exception.ResourceNotFoundException;
import com.resource_service.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping("/get-all-resources")
    public ResponseEntity<?> getAllResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            var resources = resourceService.getAllResources(page, size, sortField, sortDirection);
            return ResponseEntity.ok(resources);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-resource-by-id/{id}")
    public ResponseEntity<?> getResourceById(@PathVariable("id") String id) {
        try {
            var resource = resourceService.getResourceById(Long.valueOf(id));
            return ResponseEntity.ok(resource);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/create-resource")
    public ResponseEntity<?> createResource(@RequestBody ResourceDto resourceDto) {
        try {
            var createdResource = resourceService.createResource(resourceDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdResource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-resource/{id}")
    public ResponseEntity<?> updateResource(
            @PathVariable("id") String id,
            @RequestBody ResourceDto resourceDto) {
        try {
            var updatedResource = resourceService.updateResource(Long.valueOf(id), resourceDto);
            return ResponseEntity.ok(updatedResource);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-resource/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable("id") String id) {
        try {
            resourceService.deleteResource(Long.valueOf(id));
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/dynamic-filter")
    public ResponseEntity<?> dynamicFilterResources(
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) Boolean availability,
            @RequestParam(required = false) Date acquisitionDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            var resources = resourceService.dynamicFilterResources(provider, type, availability, acquisitionDate, page, size, sortField, sortDirection);
            return ResponseEntity.ok(resources);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/dynamic-search")
    public ResponseEntity<?> dynamicSearchResources(
            @RequestParam String input,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            var resources = resourceService.dynamicSearchResources(input, page, size, sortField, sortDirection);
            return ResponseEntity.ok(resources);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/autocomplete-search")
    public ResponseEntity<?> autocompleteSearchInput(@RequestParam String input) {
        try {
            var suggestions = resourceService.autocompleteSearchInput(input);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

package com.resource_service.service;

import com.resource_service.dto.ResourceDto;
import com.resource_service.enums.Type;
import com.resource_service.exception.ResourceNotFoundException;
import com.resource_service.mapper.ResourceMapper;
import com.resource_service.model.Resource;
import com.resource_service.repository.ResourceRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    public ResourceDto createResource(ResourceDto resourceDto) {
        var resource = resourceMapper.toEntity(resourceDto);
        var savedResource = resourceRepository.save(resource);
        return resourceMapper.toDto(savedResource);
    }

    public ResourceDto getResourceById(Long id) {
        var resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with %d not found!", id)));
        return resourceMapper.toDto(resource);
    }

    public ResourceDto updateResource(Long id, ResourceDto resourceDto) {
        var resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with %d not found!", id)));
        var updatedResource = resourceMapper.partialUpdate(resourceDto, resource);
        var savedResource = resourceRepository.save(updatedResource);
        return resourceMapper.toDto(savedResource);
    }

    public void deleteResource(Long id) {
        var resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with %d not found!", id)));
        resourceRepository.delete(resource);
    }

    public Page<ResourceDto> getAllResources(int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        var resources = resourceRepository.findAll(pageable);
        if (resources.getContent().isEmpty()) {
            throw new ResourceNotFoundException("Resources not found!");
        }
        return resources.map(resourceMapper::toDto);
    }

    public Page<Resource> dynamicFilterResources(String provider, Type type, Boolean availability, Date acquisitionDate,
                                                    int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Specification<Resource> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (provider != null && !provider.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("provider")), "%" + provider.toLowerCase() + "%"));
            }

            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            if (availability != null) {
                predicates.add(criteriaBuilder.equal(root.get("availability"), availability));
            }

            if (acquisitionDate != null) {
                predicates.add(criteriaBuilder.equal(root.get("acquisitionDate"), acquisitionDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return resourceRepository.findAll(spec, pageable);
    }

    public Page<Resource> dynamicSearchResources(String input, int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Specification<Resource> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (input != null && !input.isEmpty()) {
                String searchPattern = "%" + input.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("provider")), searchPattern)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return resourceRepository.findAll(spec, pageable);
    }

    public List<String> autocompleteSearchInput(String input) {
        if (input == null || input.isEmpty()) {
            return List.of();
        }
        return resourceRepository.findAutocompleteSuggestions(input);
    }
}

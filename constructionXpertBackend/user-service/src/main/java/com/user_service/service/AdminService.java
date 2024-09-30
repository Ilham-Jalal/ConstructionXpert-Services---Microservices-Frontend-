package com.user_service.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.user_service.dto.AdminDto;
import com.user_service.exception.AdminNotFoundException;
import com.user_service.mapper.UserMapper;
import com.user_service.model.Admin;
import com.user_service.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Page<Admin> getAllAdmins(int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return adminRepository.findAll(pageable);
    }

//    public Page<AdminDto> searchAdmins(String input, int page, int size, String sortField, String sortDirection) {
//        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
//
//        QAdmin admin = QAdmin.admin;
//        BooleanExpression predicate = admin.name.containsIgnoreCase(input) // Add other fields as necessary
//                .or(admin.email.containsIgnoreCase(input));
//
//        return adminRepository.findAll(predicate, pageable).map(adminMapper::toDto);
//    }
//
//    public Page<AdminDto> filterAdmins(String name, String email, int page, int size, String sortField, String sortDirection) {
//        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
//
//        QAdmin admin = QAdmin.admin;
//        BooleanExpression predicate = admin.isNotNull(); // Start with a base expression
//
//        if (name != null && !name.isEmpty()) {
//            predicate = predicate.and(admin.name.containsIgnoreCase(name));
//        }
//
//        if (email != null && !email.isEmpty()) {
//            predicate = predicate.and(admin.email.containsIgnoreCase(email));
//        }
//
//        return adminRepository.findAll(predicate, pageable).map(adminMapper::toDto);
//    }
//
//    public List<String> autocompleteAdmin(String input) {
//        QAdmin admin = QAdmin.admin;
//
//        return queryFactory.select(admin.name) // Adjust the field based on your needs
//                .from(admin)
//                .where(admin.name.containsIgnoreCase(input))
//                .fetch();
//    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
    }

    public AdminDto updateAdmin(Long id, AdminDto adminDto) {
        var existingAdmin = adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
        var updatedAdmin = (Admin) adminMapper.partialUpdate(adminDto, existingAdmin);
        updatedAdmin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        var savedAdmin = adminRepository.save(updatedAdmin);
        return (AdminDto) adminMapper.toDto(savedAdmin);
    }

    public void deleteAdmin(Long id) {
        var admin = adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
        adminRepository.delete(admin);
    }
}

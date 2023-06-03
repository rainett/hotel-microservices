package io.rainett.adminmicroservice.service;

import io.rainett.adminmicroservice.dto.AdminDto;
import io.rainett.adminmicroservice.exception.AdminNotFoundException;
import io.rainett.adminmicroservice.model.Admin;
import io.rainett.adminmicroservice.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Page<AdminDto> getAllAdminsByPage(Pageable page) {
        return adminRepository.findAll(page).map(this::mapToDto);
    }

    @Override
    public AdminDto getAdminById(Long id) {
        return adminRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new AdminNotFoundException(id));
    }

    @Override
    public AdminDto updateAdminById(Long id, AdminDto adminDto) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException(id));
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        admin.setEmail(adminDto.getEmail());
        admin = adminRepository.save(admin);
        return mapToDto(admin);
    }

    @Override
    public void deleteAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException(id));
        adminRepository.delete(admin);
    }

    private AdminDto mapToDto(Admin admin) {
        return AdminDto.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .password(admin.getPassword())
                .email(admin.getEmail())
                .createdAt(admin.getCreatedAt())
                .build();
    }
}

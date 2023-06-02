package io.rainett.adminmicroservice.service;

import io.rainett.adminmicroservice.dto.AdminDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<AdminDto> getAllAdminsByPage(Pageable page);

    AdminDto getAdminById(Long id);

    AdminDto updateAdminById(Long id, AdminDto adminDto);

    void deleteAdminById(Long id);
}

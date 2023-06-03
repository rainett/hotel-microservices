package io.rainett.adminmicroservice.controller;

import io.rainett.adminmicroservice.dto.AdminDto;
import io.rainett.adminmicroservice.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<Page<AdminDto>> getAllAdminsByPage(Pageable page) {
        Page<AdminDto> adminDtoPage = adminService.getAllAdminsByPage(page);
        return ResponseEntity.ok(adminDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDto> getAdminById(@PathVariable Long id) {
        AdminDto adminDto = adminService.getAdminById(id);
        return ResponseEntity.ok(adminDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDto> updateAdminById(@PathVariable Long id, @Valid @RequestBody AdminDto adminDto) {
        AdminDto updatedAdminDto = adminService.updateAdminById(id, adminDto);
        return ResponseEntity.ok(updatedAdminDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminById(@PathVariable Long id) {
        adminService.deleteAdminById(id);
        return ResponseEntity.noContent().build();
    }


}

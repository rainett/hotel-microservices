package io.rainett.managermicroservice.controller;


import io.rainett.managermicroservice.dto.ManagerDto;
import io.rainett.managermicroservice.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping
    public ResponseEntity<Page<ManagerDto>> getAllManagersByPage(Pageable pageable) {
        Page<ManagerDto> managerDtoPage = managerService.getAllManagersByPage(pageable);
        return ResponseEntity.ok(managerDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDto> getManagerById(@PathVariable Long id) {
        ManagerDto managerDto = managerService.getManagerById(id);
        return ResponseEntity.ok(managerDto);
    }

    @PostMapping
    public ResponseEntity<ManagerDto> createManager(@Valid @RequestBody ManagerDto managerDto) {
        ManagerDto createdManagerDto = managerService.createManager(managerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdManagerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagerDto> updateManagerById(@PathVariable Long id,
                                                        @Valid @RequestBody ManagerDto managerDto) {
        ManagerDto updatedManagerDto = managerService.updateManagerById(id, managerDto);
        return ResponseEntity.ok(updatedManagerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManagerById(@PathVariable Long id) {
        managerService.deleteManagerById(id);
        return ResponseEntity.noContent().build();
    }
}

package io.rainett.managermicroservice.service;

import io.rainett.managermicroservice.dto.ManagerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManagerService {
    Page<ManagerDto> getAllManagersByPage(Pageable pageable);

    ManagerDto getManagerById(Long id);

    ManagerDto createManager(ManagerDto managerDto);

    ManagerDto updateManagerById(Long id, ManagerDto managerDto);

    void deleteManagerById(Long id);
}

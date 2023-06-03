package io.rainett.managermicroservice.service;

import io.rainett.managermicroservice.dto.ManagerDto;
import io.rainett.managermicroservice.exception.ManagerNotFoundException;
import io.rainett.managermicroservice.model.Manager;
import io.rainett.managermicroservice.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    @Override
    public Page<ManagerDto> getAllManagersByPage(Pageable pageable) {
        return managerRepository.findAll(pageable).map(this::mapToDto);
    }

    @Override
    public ManagerDto getManagerById(Long id) {
        return managerRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ManagerNotFoundException(id));
    }

    @Override
    public ManagerDto createManager(ManagerDto managerDto) {
        Manager manager = new Manager();
        return persistManagerAndReturnDto(managerDto, manager);
    }

    @Override
    public ManagerDto updateManagerById(Long id, ManagerDto managerDto) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new ManagerNotFoundException(id));
        return persistManagerAndReturnDto(managerDto, manager);
    }

    @Override
    public void deleteManagerById(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new ManagerNotFoundException(id));
        managerRepository.delete(manager);
    }

    private ManagerDto persistManagerAndReturnDto(ManagerDto managerDto, Manager manager) {
        manager.setFirstName(managerDto.getFirstName());
        manager.setLastName(managerDto.getLastName());
        manager.setEmail(managerDto.getEmail());
        manager.setPassword(managerDto.getPassword());
        manager = managerRepository.save(manager);
        return mapToDto(manager);
    }

    private ManagerDto mapToDto(Manager manager) {
        return ManagerDto.builder()
                .id(manager.getId())
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .email(manager.getEmail())
                .password(manager.getPassword())
                .createdAt(manager.getCreatedAt())
                .build();
    }
}

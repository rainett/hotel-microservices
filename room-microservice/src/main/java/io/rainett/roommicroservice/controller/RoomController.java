package io.rainett.roommicroservice.controller;

import io.rainett.roommicroservice.dto.RoomDto;
import io.rainett.roommicroservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<Page<RoomDto>> getAllRoomsByPage(Pageable pageable) {
        Page<RoomDto> roomDtoPage = roomService.getAllRoomsByPage(pageable);
        return ResponseEntity.ok(roomDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        RoomDto roomDto = roomService.getRoomById(id);
        return ResponseEntity.ok(roomDto);
    }

    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody RoomDto roomDto) {
        RoomDto createdRoomDto = roomService.createRoom(roomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoomDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDto> updateRoomById(@PathVariable Long id,
                                                        @Valid @RequestBody RoomDto roomDto) {
        RoomDto updatedRoomDto = roomService.updateRoomById(id, roomDto);
        return ResponseEntity.ok(updatedRoomDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long id) {
        roomService.deleteRoomById(id);
        return ResponseEntity.noContent().build();
    }
}


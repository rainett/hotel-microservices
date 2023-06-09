package io.rainett.roommicroservice.service;

import io.rainett.roommicroservice.dto.RoomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {
    Page<RoomDto> getAllRoomsByPage(Pageable pageable);

    RoomDto getRoomById(Long id);

    RoomDto createRoom(RoomDto roomDto);

    RoomDto updateRoomById(Long id, RoomDto roomDto);

    void deleteRoomById(Long id);
}

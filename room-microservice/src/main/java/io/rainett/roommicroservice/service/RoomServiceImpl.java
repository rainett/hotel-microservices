package io.rainett.roommicroservice.service;


import io.rainett.roommicroservice.dto.RoomDto;
import io.rainett.roommicroservice.exception.RoomNotFoundException;
import io.rainett.roommicroservice.model.Room;
import io.rainett.roommicroservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public Page<RoomDto> getAllRoomsByPage(Pageable pageable) {
        return roomRepository.findAll(pageable).map(this::mapToDto);
    }

    @Override
    public RoomDto getRoomById(Long id) {
        return roomRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RoomNotFoundException(id));
    }

    @Override
    public RoomDto createRoom(RoomDto roomDto) {
        Room room = new Room();
        return persistRoomAndReturnDto(roomDto, room);
    }

    @Override
    public RoomDto updateRoomById(Long id, RoomDto roomDto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        return persistRoomAndReturnDto(roomDto, room);
    }

    @Override
    public void deleteRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        roomRepository.delete(room);
    }

    private RoomDto persistRoomAndReturnDto(RoomDto roomDto, Room room) {
        room.setNumber(roomDto.getNumber());
        room.setFloor(roomDto.getFloor());
        room.setPositionOnFloor(roomDto.getPositionOnFloor());
        room.setRoomType(roomDto.getRoomType());
        room.setRoomState(roomDto.getRoomState());
        room.setResidentsIds(roomDto.getResidentsIds());
        room.setResidentsNumber(roomDto.getResidentsNumber());
        room.setPrice(roomDto.getPrice());
        room.setCreatedAt(roomDto.getCreatedAt());
        room = roomRepository.save(room);
        return mapToDto(room);
    }

    private RoomDto mapToDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .number(room.getNumber())
                .floor(room.getFloor())
                .positionOnFloor(room.getPositionOnFloor())
                .roomType(room.getRoomType())
                .roomState(room.getRoomState())
                .residentsIds(room.getResidentsIds())
                .residentsNumber(room.getResidentsNumber())
                .price(room.getPrice())
                .createdAt(room.getCreatedAt())
                .build();
    }
}
package io.rainett.reservationmicroservice.service;

import io.rainett.reservationmicroservice.client.RoomClient;
import io.rainett.reservationmicroservice.dto.ReservationDto;
import io.rainett.reservationmicroservice.exception.ReservationNotFoundException;
import io.rainett.reservationmicroservice.exception.RoomNotFoundException;
import io.rainett.reservationmicroservice.mapper.ReservationMapper;
import io.rainett.reservationmicroservice.messaging.KafkaConverter;
import io.rainett.reservationmicroservice.model.Reservation;
import io.rainett.reservationmicroservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final RoomClient roomClient;
    private final KafkaConverter kafkaConverter;


    @Override
    public Page<ReservationDto> getAllReservationsByPage(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(reservationMapper::toReservationDto);
    }

    @Override
    public ReservationDto getReservationById(Long id) {
        return reservationRepository.findById(id)
                .map(reservationMapper::toReservationDto)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
        Long roomId = reservationDto.getRoomId();
        RequestReplyFuture<String, String, String> roomExistsRequest
                = roomClient.roomExists(roomId);
        Reservation reservation = new Reservation();
        boolean roomExists = kafkaConverter.getValue(roomExistsRequest, Boolean.class);
        if (!roomExists) {
            throw new RoomNotFoundException(roomId);
        }
        return persistReservationAndReturnDto(reservation, reservationDto);
    }

    @Override
    public ReservationDto updateReservation(Long id, ReservationDto reservationDto) {
        Long roomId = reservationDto.getRoomId();
        RequestReplyFuture<String, String, String> roomExistsRequest
                = roomClient.roomExists(roomId);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        boolean roomExists = kafkaConverter.getValue(roomExistsRequest, Boolean.class);
        if (!roomExists) {
            throw new RoomNotFoundException(roomId);
        }

        return persistReservationAndReturnDto(reservation, reservationDto);
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        reservationRepository.delete(reservation);
    }

    private ReservationDto persistReservationAndReturnDto(Reservation reservation, ReservationDto reservationDto) {
        reservation.setGuestId(reservationDto.getGuestId());
        reservation.setRoomId(reservationDto.getRoomId());
        reservation.setCheckInDate(reservationDto.getCheckInDate());
        reservation.setCheckOutDate(reservationDto.getCheckOutDate());
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toReservationDto(reservation);
    }

}

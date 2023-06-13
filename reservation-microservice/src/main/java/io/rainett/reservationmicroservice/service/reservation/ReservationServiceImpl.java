package io.rainett.reservationmicroservice.service.reservation;

import io.rainett.reservationmicroservice.dto.ReservationDto;
import io.rainett.reservationmicroservice.exception.GuestReservationNotAllowedException;
import io.rainett.reservationmicroservice.exception.ReservationNotFoundException;
import io.rainett.reservationmicroservice.exception.RoomNotAvailableException;
import io.rainett.reservationmicroservice.exception.RoomNotFoundException;
import io.rainett.reservationmicroservice.model.Reservation;
import io.rainett.reservationmicroservice.repository.ReservationRepository;
import io.rainett.reservationmicroservice.service.guest.GuestService;
import io.rainett.reservationmicroservice.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestService guestService;
    private final RoomService roomService;


    @Override
    public Page<ReservationDto> getAllReservationsByPage(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(this::mapToDto);
    }

    @Override
    public ReservationDto getReservationById(Long id) {
        return reservationRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
        checkAvailabilityAndThrowExceptions(reservationDto);
        Reservation reservation = new Reservation();
        return persistReservationAndReturnDto(reservation, reservationDto);
    }

    @Override
    public ReservationDto updateReservation(Long id, ReservationDto reservationDto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
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
        reservation.setPaymentStatus(reservationDto.getPaymentStatus());
        reservation = reservationRepository.save(reservation);
        return mapToDto(reservation);
    }

    private void checkAvailabilityAndThrowExceptions(ReservationDto reservationDto) {
        Long guestId = reservationDto.getGuestId();
        RequestReplyFuture<String, String, String> guestReply
                = guestService.guestCanPerformReservation(guestId);
        Long roomId = reservationDto.getRoomId();
        RequestReplyFuture<String, String, String> roomReply
                = roomService.roomIsAvailable(roomId);

        boolean guestCanPerformReservation = Boolean.parseBoolean(getResponse(guestReply));
        boolean roomExists = Boolean.parseBoolean(getResponse(roomReply));
        if (!guestCanPerformReservation) {
            throw new GuestReservationNotAllowedException(guestId);
        }
        if (!roomExists) {
            throw new RoomNotFoundException(roomId);
        }

        LocalDate checkInDate = reservationDto.getCheckInDate();
        LocalDate checkOutDate = reservationDto.getCheckOutDate();
        boolean roomIsAvailable = getRoomAvailability(roomId, checkInDate, checkOutDate);
        if (!roomIsAvailable) {
            throw new RoomNotAvailableException(roomId, checkInDate, checkOutDate);
        }
    }

    private boolean getRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        return !reservationRepository.existsWhereDateBetween(roomId, checkInDate, checkOutDate);
    }

    private static String getResponse(RequestReplyFuture<String, String, String> sendResultCompletableFuture) {
        long startTime = System.currentTimeMillis();
        try {
            ConsumerRecord<String, String> result = sendResultCompletableFuture.get();
            log.info("Received response {} in {} ms", result,
                    System.currentTimeMillis() - startTime);
            return result.value();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private ReservationDto mapToDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .guestId(reservation.getGuestId())
                .roomId(reservation.getRoomId())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .paymentStatus(reservation.getPaymentStatus())
                .createdAt(reservation.getCreatedAt())
                .build();
    }
}

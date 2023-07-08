package io.rainett.reservationmicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {

    private Long id;

    @NotNull(message = "Guest ID is required")
    private Long guestId;

    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotNull(message = "Check in date is required")
    private LocalDateTime checkInDate;

    @NotNull(message = "Check in date is required")
    private LocalDateTime checkOutDate;

    private LocalDateTime createdAt;

}

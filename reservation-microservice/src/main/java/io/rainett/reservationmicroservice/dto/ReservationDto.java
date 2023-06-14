package io.rainett.reservationmicroservice.dto;

import io.rainett.reservationmicroservice.model.PaymentStatus;
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

    @NotNull
    private Long guestId;

    @NotNull
    private Long roomId;

    @NotNull
    private LocalDateTime checkInDate;

    @NotNull
    private LocalDateTime checkOutDate;

    private PaymentStatus paymentStatus = PaymentStatus.NEEDED;

    private LocalDateTime createdAt;

}

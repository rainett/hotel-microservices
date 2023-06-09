package io.rainett.reservationmicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long guestId;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private LocalDateTime checkInDate;

    @Column(nullable = false)
    private LocalDateTime checkOutDate;

    private LocalDateTime createdAt = LocalDateTime.now();

}

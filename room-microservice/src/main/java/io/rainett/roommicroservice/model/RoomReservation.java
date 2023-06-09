package io.rainett.roommicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_reservations")
public class RoomReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long guestId;

    @ManyToOne
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "room_reservations_room_fk"), nullable = false)
    private Room room;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

}

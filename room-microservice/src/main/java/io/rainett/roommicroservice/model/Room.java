package io.rainett.roommicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms", uniqueConstraints = {
        @UniqueConstraint(name = "room_number_uq", columnNames = "number")
})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Short number;

    @Column(nullable = false)
    private Short floor;

    @Column(nullable = false)
    private Short positionOnFloor;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private RoomState roomState;

    @ElementCollection
    private List<Long> residentsIds;

    private Byte residentsNumber;

    private BigDecimal price;

    private LocalDateTime createdAt = LocalDateTime.now();

}

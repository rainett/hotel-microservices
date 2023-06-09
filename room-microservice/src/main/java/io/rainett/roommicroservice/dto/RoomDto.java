package io.rainett.roommicroservice.dto;

import io.rainett.roommicroservice.model.RoomState;
import io.rainett.roommicroservice.model.RoomType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {

    private Long id;

    @NotNull(message = "Number is required")
    private Short number;

    @NotNull(message = "Floor is required")
    private Short floor;

    @NotNull(message = "Position on floor is required")
    private Short positionOnFloor;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @NotNull(message = "Room state is required")
    private RoomState roomState;

    private List<Long> residentsIds;

    private Byte residentsNumber;

    private BigDecimal price;

    private LocalDateTime createdAt;

}


package fr.bookhub.dto;

import fr.bookhub.entity.Status;

import java.time.LocalDateTime;

public record ReservationResponse(
        Integer id,
        ReservationBookResponse book,
        Integer queuePosition,
        Status status,
        LocalDateTime createdAt
) {
}

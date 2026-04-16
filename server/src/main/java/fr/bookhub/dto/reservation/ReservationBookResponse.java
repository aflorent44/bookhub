package fr.bookhub.dto.reservation;

public record ReservationBookResponse(
        Integer id,
        String title,
        String author,
        String coverUrl
) {
}

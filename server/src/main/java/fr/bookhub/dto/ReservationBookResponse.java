package fr.bookhub.dto;

public record ReservationBookResponse(
        Integer id,
        String title,
        String author,
        String coverUrl
) {
}

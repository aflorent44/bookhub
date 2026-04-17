package fr.bookhub.dto.reservation;

public record ReservationCreateRequest (
        Integer userId,
        Integer bookId
) {

}

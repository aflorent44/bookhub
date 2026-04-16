package fr.bookhub.dto.book;

public record BookBasicResponse(
        Integer id,
        String isbn,
        String title,
        String authorLastName,
        String authorFirstName
) {
}

package fr.bookhub.dto;

public record BookBasicResponse(
        Integer id,
        String isbn,
        String title,
        String authorLastName,
        String authorFirstName
) {
}

package fr.bookhub.dto;

public record TopBookResponse(
        Integer id,
        String isbn,
        String title,
        String authorLastName,
        String authorFirstName,
        Long totalLoans
) {
}

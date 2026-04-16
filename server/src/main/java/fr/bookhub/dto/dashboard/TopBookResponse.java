package fr.bookhub.dto.dashboard;

public record TopBookResponse(
        Integer id,
        String isbn,
        String title,
        String authorLastName,
        String authorFirstName,
        Long totalLoans
) {
}

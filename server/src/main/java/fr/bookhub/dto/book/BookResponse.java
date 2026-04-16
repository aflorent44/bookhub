package fr.bookhub.dto.book;

import fr.bookhub.dto.genre.GenreDTO;
import fr.bookhub.entity.Country;
import fr.bookhub.entity.User;

import java.time.LocalDateTime;
import java.util.Set;

public record BookResponse (
        Integer bookId,
        String isbn,
        String title,
        Integer year,
        Integer quantity,
        String description,
        String firstPageUrl,
        Country country,
        String authorFirstName,
        String authorLastName,
        String publisherName,
        Set<GenreDTO> genres,
        User createdBy,
        User updatedBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
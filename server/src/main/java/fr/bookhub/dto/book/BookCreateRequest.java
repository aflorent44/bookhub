package fr.bookhub.dto.book;

import fr.bookhub.dto.genre.GenreDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

public record BookCreateRequest (
        Integer bookId,
        String isbn,
        String title,
        Integer year,
        Integer quantity,
        String description,
        String firstPageUrl,

        String authorFirstName,
        String authorLastName,

        String publisherName,

        String countryName,

        Set<GenreDTO> genres,

        Integer createdById,
        Integer updatedById
) {
}

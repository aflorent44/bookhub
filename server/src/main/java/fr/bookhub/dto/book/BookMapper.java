package fr.bookhub.dto.book;

import fr.bookhub.dto.genre.GenreDTO;
import fr.bookhub.entity.Book;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getYear(),
                book.getQuantity(),
                book.getDescription(),
                book.getFirstPageUrl(),
                book.getCountry(),
                book.getAuthor().getFirstName(),
                book.getAuthor().getLastName(),
                book.getPublisher().getName(),
                book.getGenres().stream()
                        .map(g -> new GenreDTO(g.getId(), g.getLabel()))
                        .collect(Collectors.toSet()),
                book.getCreatedBy(),
                book.getUpdatedBy(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}

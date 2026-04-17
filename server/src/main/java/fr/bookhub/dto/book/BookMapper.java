package fr.bookhub.dto.book;

import fr.bookhub.dto.genre.GenreDTO;
import fr.bookhub.entity.Book;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookResponse toResponse(Book book) {
        String authorFirstName = book.getAuthor().getFirstName() == null ? "" : book.getAuthor().getFirstName();
        String authorLastName = book.getAuthor().getLastName() == null ? "" : book.getAuthor().getLastName();
        String publisher = book.getPublisher() == null ? "" : book.getPublisher().getName();

        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getYear(),
                book.getQuantity(),
                book.getDescription(),
                book.getFirstPageUrl(),
                book.getCountry(),
                authorFirstName,
                authorLastName,
                publisher,
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

package fr.bookhub.service;

import fr.bookhub.entity.Book;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookResponse toResponse(Book book) {
        BookResponse dto = new BookResponse();

        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setYear(book.getYear());
        dto.setQuantity(book.getQuantity());
        dto.setDescription(book.getDescription());

        dto.setAuthorFirstName(book.getAuthor().getFirstName());
        dto.setAuthorLastName(book.getAuthor().getLastName());

        dto.setPublisherName(book.getPublisher().getName());

        dto.setGenres(
                book.getGenres().stream()
                        .map(g -> new GenreDTO(g.getId(), g.getLabel()))
                        .collect(Collectors.toSet())
        );

        return dto;
    }
}

package fr.bookhub.dto;

import fr.bookhub.entity.Book;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookResponse toResponse(Book book) {
        BookResponse dto = new BookResponse();

        dto.setBookId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setYear(book.getYear());
        dto.setQuantity(book.getQuantity());
        dto.setDescription(book.getDescription());
        dto.setFirstPageUrl(book.getFirstPageUrl());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setCreatedBy(book.getCreatedBy());
        dto.setUpdatedAt(book.getUpdatedAt());
        dto.setUpdatedBy(book.getUpdatedBy());
        dto.setCountry(book.getCountry());

        if (book.getAuthor() != null) {
            dto.setAuthorFirstName(book.getAuthor().getFirstName());
            dto.setAuthorLastName(book.getAuthor().getLastName());
        }

        if (book.getPublisher() != null) {
            dto.setPublisherName(book.getPublisher().getName());
        }

        dto.setGenres(
                book.getGenres().stream()
                        .map(g -> new GenreDTO(g.getId(), g.getLabel()))
                        .collect(Collectors.toSet())
        );

        return dto;
    }

    public BookResponse toBasicResponse(Book book) {
        BookResponse dto = new BookResponse();

        dto.setBookId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());

        if (book.getAuthor() != null) {
            dto.setAuthorFirstName(book.getAuthor().getFirstName());
            dto.setAuthorLastName(book.getAuthor().getLastName());
        }

        dto.setGenres(
                book.getGenres().stream()
                        .map(g -> new GenreDTO(g.getId(), g.getLabel()))
                        .collect(Collectors.toSet())
        );

        return dto;
    }
}

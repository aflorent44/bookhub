package fr.bookhub.dto;

import fr.bookhub.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookBasicMapper {

    public BookBasicResponse toResponse(Book book) {
        return new BookBasicResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor().getLastName(),
                book.getAuthor().getFirstName()
        );
    }
}

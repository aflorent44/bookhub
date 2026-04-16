package fr.bookhub.dto.loan;

import fr.bookhub.dto.user.UserBasicResponse;
import fr.bookhub.dto.book.BookBasicResponse;

import java.time.LocalDateTime;

public record LoanBasicResponse(
        Integer id,
        LocalDateTime debutDate,
        LocalDateTime endDate,
        UserBasicResponse user,
        BookBasicResponse book
) {
}

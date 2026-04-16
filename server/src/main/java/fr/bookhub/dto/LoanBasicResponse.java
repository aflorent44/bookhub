package fr.bookhub.dto;

import java.time.LocalDateTime;

public record LoanBasicResponse(
        Integer id,
        LocalDateTime debutDate,
        LocalDateTime endDate,
        UserBasicResponse user,
        BookBasicResponse book
) {
}

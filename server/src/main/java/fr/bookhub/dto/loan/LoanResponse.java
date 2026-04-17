package fr.bookhub.dto.loan;

import fr.bookhub.dto.user.UserBasicResponse;
import fr.bookhub.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public record LoanResponse (
        Integer id,
        LocalDateTime debutDate,
        LocalDateTime endDate,
        LocalDateTime returnDate,
        Status status,
        boolean late,
        Integer userId,
        Integer bookId,
        String bookTitle,
        String bookAuthor,
        String bookCoverUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserBasicResponse updatedBy
) {

}

package fr.bookhub.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record LoanCreateRequest(
        Integer userId,
        Integer internalUserId,
        Integer bookId,
        Integer loanId
) {
}

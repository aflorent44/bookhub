package fr.bookhub.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanCreateRequest {
    private Integer loanId;

    @Positive(message = "UserId must be positive")
    private Integer userId;

    @NotNull(message = "InternalUserId is required")
    @Positive(message = "InternalUserId must be positive")
    private Integer internalUserId;

    @NotNull(message = "BookId is required")
    @Positive(message = "BookId must be positive")
    private Integer bookId;
}

package fr.bookhub.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanCreateRequest {
    private Integer userId;
    private Integer internalUserId;
    private Integer bookId;
    private Integer loanId;
}

package fr.bookhub.service;

import fr.bookhub.entity.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanResponse toResponse(Loan loan) {
        LoanResponse dto = new LoanResponse();

        dto.setId(loan.getId());
        dto.setDebutDate(loan.getDebutDate());
        dto.setEndDate(loan.getEndDate());
        dto.setStatus(loan.getStatus());
        dto.setUserId(loan.getUser().getId());
        dto.setBookId(loan.getBook().getId());
        dto.setCreatedAt(loan.getCreatedAt());
        dto.setUpdatedAt(loan.getUpdatedAt());
        dto.setUpdatedBy(loan.getUpdatedBy().getId());

        return dto;
    }
}

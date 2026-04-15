package fr.bookhub.service;

import fr.bookhub.entity.Loan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanMapper {

    public LoanResponse toResponse(Loan loan) {
        LoanResponse dto = new LoanResponse();

        dto.setId(loan.getId());
        dto.setDebutDate(loan.getDebutDate());
        dto.setEndDate(loan.getEndDate());
        dto.setReturnDate(loan.getReturnDate());
        dto.setStatus(loan.getStatus());
        dto.setUserId(loan.getUser().getId());
        dto.setBookId(loan.getBook().getId());
        dto.setCreatedAt(loan.getCreatedAt());

        if (loan.getUpdatedBy() != null) {
            dto.setUpdatedBy(loan.getUpdatedBy().getId());
            dto.setUpdatedAt(loan.getUpdatedAt());
        }

        return dto;
    }

    public List<LoanResponse> toResponse(List<Loan> loans) {
        return loans.stream()
                .map(this::toResponse)
                .toList();
    }
}

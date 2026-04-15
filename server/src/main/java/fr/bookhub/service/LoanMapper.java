package fr.bookhub.service;

import fr.bookhub.dto.LoanResponse;
import fr.bookhub.entity.Loan;
import fr.bookhub.entity.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
        dto.setBookTitle(loan.getBook().getTitle());
        dto.setBookAuthor(loan.getBook().getAuthor().getFirstName() + " " + loan.getBook().getAuthor().getLastName());
        dto.setBookCoverUrl(loan.getBook().getFirstPageUrl());

        dto.setCreatedAt(loan.getCreatedAt());

        boolean isLate =
                loan.getStatus() == Status.IN_PROGRESS
                        && loan.getEndDate() != null
                        && loan.getEndDate().isBefore(LocalDateTime.now());

        dto.setLate(isLate);

        if (loan.getUpdatedBy() != null) {
            dto.setUpdatedBy(loan.getUpdatedBy().getId());
        }

        if (loan.getUpdatedAt() != null) {
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
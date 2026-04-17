package fr.bookhub.dto.loan;

import fr.bookhub.dto.user.UserBasicMapper;
import fr.bookhub.entity.Loan;
import fr.bookhub.entity.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LoanMapper {

    public LoanResponse toResponse(Loan loan) {
        boolean isLate =
                loan.getStatus() == Status.IN_PROGRESS
                        && loan.getEndDate() != null
                        && loan.getEndDate().isBefore(LocalDateTime.now());


        return new LoanResponse(
                loan.getId(),
                loan.getDebutDate(),
                loan.getEndDate(),
                loan.getReturnDate(),
                loan.getStatus(),

                isLate,

                loan.getUser().getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getBook().getAuthor().getFirstName() + " " + loan.getBook().getAuthor().getLastName(),
                loan.getBook().getFirstPageUrl(),

                loan.getCreatedAt(),
                loan.getUpdatedAt(),
                new UserBasicMapper().toResponse(loan.getUpdatedBy()
        ));
    }

    public List<LoanResponse> toResponse(List<Loan> loans) {
        return loans.stream()
                .map(this::toResponse)
                .toList();
    }
}
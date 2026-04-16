package fr.bookhub.dto;

import fr.bookhub.entity.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanBasicMapper {

    private final UserBasicMapper userBasicMapper = new UserBasicMapper();
    private final BookBasicMapper bookBasicMapper = new BookBasicMapper();

    public LoanBasicResponse toResponse(Loan loan) {
        return new LoanBasicResponse (
            loan.getId(),
            loan.getDebutDate(),
            loan.getEndDate(),
            userBasicMapper.toResponse(loan.getUser()),
            bookBasicMapper.toResponse(loan.getBook())
        );
    }
}

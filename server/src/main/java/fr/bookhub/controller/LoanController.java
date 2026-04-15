package fr.bookhub.controller;

import fr.bookhub.service.LoanCreateRequest;
import fr.bookhub.service.LoanService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/api/loan")
    public ServiceResponse<?> loanBook(@RequestBody LoanCreateRequest req) {
        return loanService.createLoan(req);
    }

    @PostMapping("/api/loan/return")
    public ServiceResponse<?> returnBook(@RequestBody LoanCreateRequest req) {
        return loanService.finishLoan(req);
    }

    @GetMapping("/api/loan/{bookId}")
    public ServiceResponse<List<?>> getLoansByBookId(@PathVariable int bookId) {
        return loanService.getLoansByBookId(bookId);
    }

    @PostMapping("/api/loan/validate")
    public ServiceResponse<?> validateLoan(@RequestBody LoanCreateRequest req) { return loanService.validate(req); }

    @PostMapping("/api/loan/delete/{id}")
    public ServiceResponse<?> cancelLoan(@PathVariable int id) {
        return loanService.deleteLoan(id);
    }

    @GetMapping("/api/loan/user/{userId}/book/{bookId}")
    public ServiceResponse<?> getLoansByUserIdAndBookId(@PathVariable int userId, @PathVariable int bookId) {
        return loanService.getLoansByUserIdAndBookId(userId, bookId);
    }
}

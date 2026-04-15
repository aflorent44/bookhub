package fr.bookhub.controller;

import fr.bookhub.dto.LoanCreateRequest;
import fr.bookhub.entity.Status;
import fr.bookhub.service.LoanService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/loan")
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ServiceResponse<?> loanBook(@RequestBody LoanCreateRequest req) {
        return loanService.createLoan(req);
    }

    @PostMapping("/return")
    public ServiceResponse<?> returnBook(@RequestBody LoanCreateRequest req) {
        return loanService.finishOrCancelLoan(req, Status.FINISHED);
    }

    @GetMapping("/{bookId}")
    public ServiceResponse<List<?>> getLoansByBookId(@PathVariable int bookId) {
        return loanService.getLoansByBookId(bookId);
    }

    @PostMapping("/validate")
    public ServiceResponse<?> validateLoan(@RequestBody LoanCreateRequest req) { return loanService.validate(req); }

    @PostMapping("/cancel")
    public ServiceResponse<?> cancelLoan(@RequestBody LoanCreateRequest req) {
        return loanService.finishOrCancelLoan(req, Status.CANCELED);
    }

    @GetMapping("/user/{userId}/book/{bookId}")
    public ServiceResponse<?> getLoansByUserIdAndBookId(@PathVariable int userId, @PathVariable int bookId) {
        return loanService.getLoansByUserIdAndBookId(userId, bookId);
    }
}

package fr.bookhub.controller;

import fr.bookhub.service.LoanCreateRequest;
import fr.bookhub.service.LoanService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/api/loan/validate")
    public ServiceResponse<?> validateLoan(@RequestBody LoanCreateRequest req) { return loanService.validate(req); }

    @PostMapping("/api/loan/delete/{id}")
    public ServiceResponse<?> cancelLoan(@PathVariable int id) {
        return loanService.deleteLoan(id);
    }
}

package fr.bookhub.controller;

import fr.bookhub.service.LoanCreateRequest;
import fr.bookhub.service.LoanService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
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
}

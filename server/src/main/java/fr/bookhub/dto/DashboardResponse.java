package fr.bookhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    long totalBooks;
    List<LoanBasicResponse> activeLoans;
    List<LoanBasicResponse> lateLoans;
    List<TopBookResponse> topBooks;

}

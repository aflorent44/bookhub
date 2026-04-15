package fr.bookhub.service;

import fr.bookhub.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {
    private Integer id;
    private LocalDateTime debutDate;
    private LocalDateTime endDate;
    private LocalDateTime returnDate;
    private Status status;
    private Integer userId;
    private Integer bookId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer updatedBy;
}

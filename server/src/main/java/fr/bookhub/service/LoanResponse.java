package fr.bookhub.service;

import fr.bookhub.entity.Book;
import fr.bookhub.entity.Status;
import fr.bookhub.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Status status;
    private Integer userId;
    private Integer bookId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer updatedBy;
}

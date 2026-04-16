package fr.bookhub.dto;

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
    private boolean late;
    private Integer userId;
    private Integer bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookCoverUrl;
    private LocalDateTime createdAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}
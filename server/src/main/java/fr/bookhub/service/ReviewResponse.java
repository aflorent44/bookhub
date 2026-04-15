package fr.bookhub.service;

import fr.bookhub.entity.Book;
import fr.bookhub.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Integer reviewId;
    private Integer bookId;
    private UserBasicResponse user;
    private Integer rating;
    private String comment;
    private Boolean isHidden;
    private UserBasicResponse hiddenBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserBasicResponse updatedBy;
}

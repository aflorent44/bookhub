package fr.bookhub.service;

import fr.bookhub.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

    private Integer reviewId;
    private Integer bookId;
    private Integer userId;
    private Integer internalUserId;
    private Integer rating;
    private String comment;
    private Boolean isHidden;
}

package fr.bookhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

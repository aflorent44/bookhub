package fr.bookhub.dto.review;

import fr.bookhub.dto.user.UserBasicResponse;

import java.time.LocalDateTime;

public record ReviewResponse (
        Integer reviewId,
        Integer bookId,
        UserBasicResponse user,
        Integer rating,
        String comment,
        Boolean isHidden,
        UserBasicResponse hiddenBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserBasicResponse updatedBy
) {

}

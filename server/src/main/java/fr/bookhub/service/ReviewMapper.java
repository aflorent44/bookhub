package fr.bookhub.service;

import fr.bookhub.entity.Review;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReviewMapper {

    private final UserBasicMapper userMapper = new UserBasicMapper();

    public ReviewResponse toResponse(Review review) {
        ReviewResponse dto = new ReviewResponse();

        dto.setReviewId(review.getId());
        dto.setBookId(review.getBook().getId());
        dto.setUser(userMapper.toResponse(review.getUser()));
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setIsHidden(review.getIsHidden());
        dto.setHiddenBy(userMapper.toResponse(review.getHiddenBy()));
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        dto.setUpdatedBy(userMapper.toResponse(review.getUpdatedBy()));

        return dto;
    }
}

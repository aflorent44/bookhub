package fr.bookhub.dto;

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
        dto.setHiddenBy(review.getHiddenBy() != null ? userMapper.toResponse(review.getHiddenBy()) : null);
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        dto.setUpdatedBy(review.getUpdatedBy() != null ? userMapper.toResponse(review.getUpdatedBy()) : null);

        return dto;
    }
}

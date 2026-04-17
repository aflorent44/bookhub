package fr.bookhub.dto.review;

import fr.bookhub.dto.user.UserBasicMapper;
import fr.bookhub.entity.Review;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReviewMapper {

    private final UserBasicMapper userMapper = new UserBasicMapper();

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getBook().getId(),
                userMapper.toResponse(review.getUser()),
                review.getRating(),
                review.getComment(),
                review.getIsHidden(),
                userMapper.toResponse(review.getHiddenBy()),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                userMapper.toResponse(review.getUpdatedBy())
        );
    }
}

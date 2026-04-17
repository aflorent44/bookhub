package fr.bookhub.dto.review;


public record ReviewCreateRequest (
        Integer reviewId,
        Integer bookId,
        Integer userId,
        Integer internalUserId,
        Integer rating,
        String comment,
        Boolean isHidden
) {
}

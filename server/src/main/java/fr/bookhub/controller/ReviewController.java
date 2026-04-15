package fr.bookhub.controller;

import fr.bookhub.entity.Review;
import fr.bookhub.service.MethodType;
import fr.bookhub.service.ReviewCreateRequest;
import fr.bookhub.service.ReviewService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/api/review")
    public ServiceResponse<?> createReview(@RequestBody ReviewCreateRequest req) {
        return reviewService.createOrUpdateReview(req, MethodType.CREATE);
    }

    @PostMapping("/api/review/update")
    public ServiceResponse<?> updateReview(@RequestBody ReviewCreateRequest req) {
        return reviewService.createOrUpdateReview(req, MethodType.UPDATE);
    }

}

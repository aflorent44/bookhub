package fr.bookhub.controller;

import fr.bookhub.entity.Review;
import fr.bookhub.service.MethodType;
import fr.bookhub.service.ReviewCreateRequest;
import fr.bookhub.service.ReviewService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("")
    public ServiceResponse<?> createReview(@RequestBody ReviewCreateRequest req) {
        return reviewService.createOrUpdateReview(req, MethodType.CREATE);
    }

    @PostMapping("/update")
    public ServiceResponse<?> updateReview(@RequestBody ReviewCreateRequest req) {
        return reviewService.createOrUpdateReview(req, MethodType.UPDATE);
    }

    @PostMapping("/delete/{id}")
    public ServiceResponse<?> deleteReview(@PathVariable int id) {
        return reviewService.delete(id);
    }
}

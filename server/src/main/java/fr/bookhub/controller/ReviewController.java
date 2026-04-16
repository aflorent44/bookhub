package fr.bookhub.controller;

import fr.bookhub.utility.MethodType;
import fr.bookhub.dto.ReviewCreateRequest;
import fr.bookhub.service.ReviewService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{bookId}")
    public ServiceResponse<?> getReviewsByBookId(@PathVariable Integer bookId) {
        return reviewService.getReviewsByBookId(bookId);
    }

    @PostMapping("")
    public ServiceResponse<?> createReview(@RequestBody ReviewCreateRequest req) {
        return reviewService.createOrUpdateReview(req, MethodType.CREATE);
    }

    @PutMapping("/update")
    public ServiceResponse<?> updateReview(@RequestBody ReviewCreateRequest req) {
        return reviewService.createOrUpdateReview(req, MethodType.UPDATE);
    }

    @DeleteMapping("/delete/{id}")
    public ServiceResponse<?> deleteReview(@PathVariable int id) {
        return reviewService.delete(id);
    }
}

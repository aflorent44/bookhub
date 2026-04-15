package fr.bookhub.service;

import fr.bookhub.entity.*;
import fr.bookhub.repository.BookRepository;
import fr.bookhub.repository.LoanRepository;
import fr.bookhub.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewService {

    private final UserService userService;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final LoanRepository loanRepository;

    public ServiceResponse<?> createOrUpdateReview(ReviewCreateRequest req, MethodType method) {
        // Vérifier si l'utilisateur existe :
        ServiceResponse<User> userResponse = userService.getUserById(req.getUserId());
        if (userResponse.getCode().equals("8001")) {
            return userResponse;
        }

        // Vérifier si l'internalUser existe :
        ServiceResponse<User> internalUserResponse = userService.getUserById(req.getInternalUserId());
        if (internalUserResponse.getCode().equals("8001")) {
            return internalUserResponse;
        }

        // Vérifier si le livre existe :
        Optional<Book> foundBook = bookRepository.findById(req.getBookId());
        if (foundBook.isEmpty()) {
            return new ServiceResponse<>("10001", "Book not found");
        }

        // Vérifier si l'utilisateur à emprunter ce livre :
        if (method == MethodType.CREATE) {
            List<Loan> loans = loanRepository.findByUserIdAndBookId(req.getUserId(), req.getBookId());

            if (loans.isEmpty()) {
                return new ServiceResponse<>("10004", "User unauthorized to review");
            }

            boolean hasValidLoan = loans.stream()
                    .anyMatch(loan -> EnumSet.of(Status.FINISHED, Status.IN_PROGRESS)
                            .contains(loan.getStatus()));

            if (!hasValidLoan) {
                return new ServiceResponse<>("10004", "User unauthorized to review");
            }
        }


        // Vérification de la note : (doit être comprise entre 1 et 5) :
        if (req.getRating() <= 0 || req.getRating() > 5) {
            return new ServiceResponse<>("10002", "Invalid rating");
        }

        Review review;

        if (method == MethodType.CREATE) {
            review = new Review();
            review.setIsHidden(false);
            review.setHiddenBy(null);
            review.setCreatedAt(LocalDateTime.now());
        } else {
            Optional<Review> foundReview = reviewRepository.findById(req.getReviewId());

            if (foundReview.isEmpty()) {
                return new  ServiceResponse<>("10003", "Review not found");
            }

            review = foundReview.get();

            review.setIsHidden(req.getIsHidden());
            review.setHiddenBy(internalUserResponse.getData());
            review.setUpdatedBy(internalUserResponse.getData());
            review.setUpdatedAt(LocalDateTime.now());
        }

        review.setBook(foundBook.get());
        review.setUser(userResponse.getData());
        review.setRating(req.getRating());
        review.setComment(req.getComment());

        Review savedReview = reviewRepository.save(review);

        if (method == MethodType.CREATE) {
            return new ServiceResponse<>("10000", "Review successfully created", reviewMapper.toResponse(savedReview));
        }
        return new ServiceResponse<>("10005", "Review successfully updated", reviewMapper.toResponse(savedReview));
    }

    public ServiceResponse<?> delete(int id) {
        // Vérifier que la review existe :
        Optional<Review> foundReview = reviewRepository.findById(id);

        if (foundReview.isEmpty()) {
            return new ServiceResponse<>("10004", "Review not found");
        }

        reviewRepository.deleteById(id);

        return new ServiceResponse<>("10010", "Review successfully deleted");
    }
}

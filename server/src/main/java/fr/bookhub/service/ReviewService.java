package fr.bookhub.service;

import fr.bookhub.dto.review.ReviewCreateRequest;
import fr.bookhub.dto.review.ReviewMapper;
import fr.bookhub.entity.*;
import fr.bookhub.repository.BookRepository;
import fr.bookhub.repository.LoanRepository;
import fr.bookhub.repository.ReviewRepository;
import fr.bookhub.utility.ApiCode;
import fr.bookhub.utility.ApiException;
import fr.bookhub.utility.MethodType;
import fr.bookhub.utility.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        ServiceResponse<User> userResponse = userService.getUserById(req.userId());
        if (userResponse.getCode().equals("8001")) {
            return userResponse;
        }

        // Vérifier si l'internalUser existe :
        ServiceResponse<User> internalUserResponse = userService.getUserById(req.internalUserId());
        if (internalUserResponse.getCode().equals("8001")) {
            return internalUserResponse;
        }

        // Vérifier si le livre existe :
        Optional<Book> foundBook = bookRepository.findById(req.bookId());
        if (foundBook.isEmpty()) {
            throw new ApiException(ApiCode.REVIEW_BOOK_NOT_FOUND);
        }

        // Vérifier si l'utilisateur à emprunter ce livre :
        if (method == MethodType.CREATE) {
            List<Loan> loans = loanRepository.findByUserIdAndBookId(req.userId(), req.bookId());

            if (loans.isEmpty()) {
                throw new ApiException(ApiCode.REVIEW_USER_UNAUTHORIZED);
            }

            boolean hasValidLoan = loans.stream()
                    .anyMatch(loan -> EnumSet.of(Status.FINISHED, Status.IN_PROGRESS)
                            .contains(loan.getStatus()));

            if (!hasValidLoan) {
                throw new ApiException(ApiCode.REVIEW_USER_UNAUTHORIZED);
            }
        }


        // Vérification de la note : (doit être comprise entre 1 et 5) :
        if (req.rating() <= 0 || req.rating() > 5) {
            throw new ApiException(ApiCode.REVIEW_INVALID_RATING);
        }

        Review review;

        if (method == MethodType.CREATE) {
            review = new Review();
            review.setIsHidden(false);
            review.setHiddenBy(null);
            review.setCreatedAt(LocalDateTime.now());
        } else {
            Optional<Review> foundReview = reviewRepository.findById(req.reviewId());

            if (foundReview.isEmpty()) {
                throw new ApiException(ApiCode.REVIEW_NOT_FOUND);
            }

            review = foundReview.get();

            review.setIsHidden(req.isHidden());
            review.setHiddenBy(internalUserResponse.getData());
            review.setUpdatedBy(internalUserResponse.getData());
            review.setUpdatedAt(LocalDateTime.now());
        }

        review.setBook(foundBook.get());
        review.setUser(userResponse.getData());
        review.setRating(req.rating());
        review.setComment(req.comment());

        Review savedReview = reviewRepository.save(review);

        if (method == MethodType.CREATE) {
            return new ServiceResponse<>(ApiCode.REVIEW_CREATED, reviewMapper.toResponse(savedReview));
        }
        return new ServiceResponse<>(ApiCode.REVIEW_UPDATED, reviewMapper.toResponse(savedReview));
    }

    public ServiceResponse<?> delete(int id) {
        // Vérifier que la review existe :
        Optional<Review> foundReview = reviewRepository.findById(id);

        if (foundReview.isEmpty()) {
            throw new ApiException(ApiCode.REVIEW_NOT_FOUND);
        }

        reviewRepository.deleteById(id);

        return new ServiceResponse<>(ApiCode.REVIEW_DELETED);
    }
}

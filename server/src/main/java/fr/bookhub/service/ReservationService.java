package fr.bookhub.service;

import fr.bookhub.entity.*;
import fr.bookhub.dto.reservation.ReservationBookResponse;
import fr.bookhub.dto.reservation.ReservationCreateRequest;
import fr.bookhub.dto.reservation.ReservationResponse;
import fr.bookhub.entity.Book;
import fr.bookhub.entity.Reservation;
import fr.bookhub.entity.Status;
import fr.bookhub.entity.User;
import fr.bookhub.repository.BookRepository;
import fr.bookhub.repository.LoanRepository;
import fr.bookhub.repository.ReservationRepository;
import fr.bookhub.utility.ApiCode;
import fr.bookhub.utility.ApiException;
import fr.bookhub.utility.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {

    private final UserService userService;
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;


    public ServiceResponse<?> createReservation(String email, ReservationCreateRequest req) {
        // Récupérer l'utilisateur connecté
        User foundUser = userService.findByEmail(email);
        // Max 5 réservations / utilisateur
        List<Reservation> userReservations = reservationRepository.findByUserId(foundUser.getId());
        if (userReservations.size() >= 5) {
            throw new ApiException(ApiCode.RESERVATION_QUOTA_REACHED);
        }

        // Récupérer le livre :
        Optional<Book> book = bookRepository.findById(req.bookId());
        if (book.isEmpty()) {
            throw new ApiException(ApiCode.RESERVATION_BOOK_NOT_FOUND);
        }

        List<Reservation> existingReservations = reservationRepository.findByUserIdAndBookId(foundUser.getId(), req.bookId());

        boolean hasActiveReservation = existingReservations.stream()
                .anyMatch(r -> r.getStatus() == Status.WAITING);

        if (hasActiveReservation) {
            throw new ApiException(ApiCode.RESERVATION_ALREADY_EXISTS);
        }

        List<Loan> existingLoans = loanRepository.findByUserIdAndBookId(foundUser.getId(), req.bookId());

        boolean hasActiveLoan = existingLoans.stream()
                .anyMatch(l -> l.getStatus() == Status.WAITING || l.getStatus() == Status.IN_PROGRESS);

        if (hasActiveLoan) {
            throw new ApiException(ApiCode.RESERVATION_LOAN_CONFLICT);
        }

        // Création de la réservation :
        Book foundBook = book.get();

        Reservation reservation = new Reservation();
        reservation.setBook(foundBook);
        reservation.setUser(foundUser);
        reservation.setStatus(Status.WAITING);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        return new ServiceResponse<>(ApiCode.RESERVATION_CREATED, toResponse(savedReservation));
    }

    public ServiceResponse<?> getMyReservations(String email) {
        User foundUser = userService.findByEmail(email);

        List<Reservation> reservations = reservationRepository.findByUserId(foundUser.getId());

        List<ReservationResponse> responses = reservations.stream()
                .map(this::toResponse)
                .toList();

        return new ServiceResponse<>(ApiCode.RESERVATION_RETRIEVES, responses);
    }

    public ServiceResponse<?> deleteReservation(int reservationId) {
        // Trouver la réservation :
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if (reservation.isEmpty()) {
            throw new ApiException(ApiCode.RESERVATION_NOT_FOUND);
        }

        if (reservation.get().getStatus() != Status.WAITING) {
            throw new ApiException(ApiCode.RESERVATION_INVALID_STATUS);
        }

        reservationRepository.deleteById(reservationId);

        return new ServiceResponse<>(ApiCode.RESERVATION_DELETED);
    }

    public ServiceResponse<List<?>> getReservationsByBookId(int bookId) {
        List<Reservation> reservations = reservationRepository.findByBookId(bookId);

        if (reservations.isEmpty()) {
            throw new ApiException(ApiCode.RESERVATION_NOT_FOUND);
        }

        return new ServiceResponse<>(
                ApiCode.RESERVATION_RETRIEVES,
                reservations.stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    public ServiceResponse<List<?>> getReservationsByUserIdAndBookId(int userId, int bookId) {
        List<Reservation> reservations = reservationRepository.findByUserIdAndBookId(userId, bookId);

        if (reservations.isEmpty()) {
            throw new ApiException(ApiCode.RESERVATION_NOT_FOUND);
        }

        return new ServiceResponse<>(
                ApiCode.RESERVATION_RETRIEVES,
                reservations.stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    private int calculateQueuePosition(Reservation reservation) {
        if (reservation.getStatus() != Status.WAITING) {
            return 0;
        }

        return reservationRepository.findByBookIdAndStatusOrderByCreatedAtAsc(
                        reservation.getBook().getId(),
                        Status.WAITING
                ).stream()
                .map(Reservation::getId)
                .toList()
                .indexOf(reservation.getId()) + 1;
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return toResponse(reservation, calculateQueuePosition(reservation));
    }

    private ReservationResponse toResponse(Reservation reservation, int queuePosition) {
        Book book = reservation.getBook();

        String author = (
                (book.getAuthor().getFirstName() != null ? book.getAuthor().getFirstName() : "")
                        + " " +
                        (book.getAuthor().getLastName() != null ? book.getAuthor().getLastName() : "")
        ).trim();

        return new ReservationResponse(
                reservation.getId(),
                new ReservationBookResponse(
                        book.getId(),
                        book.getTitle(),
                        author,
                        book.getFirstPageUrl()
                ),
                queuePosition,
                reservation.getStatus(),
                reservation.getCreatedAt()
        );
    }
}
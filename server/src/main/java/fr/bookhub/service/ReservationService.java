package fr.bookhub.service;

import fr.bookhub.entity.*;
import fr.bookhub.dto.ReservationBookResponse;
import fr.bookhub.dto.ReservationCreateRequest;
import fr.bookhub.dto.ReservationResponse;
import fr.bookhub.entity.Book;
import fr.bookhub.entity.Reservation;
import fr.bookhub.entity.Status;
import fr.bookhub.entity.User;
import fr.bookhub.repository.BookRepository;
import fr.bookhub.repository.LoanRepository;
import fr.bookhub.repository.ReservationRepository;
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
            return new ServiceResponse<>("9001", "Reservation quota reached");
        }

        // Récupérer le livre :
        Optional<Book> book = bookRepository.findById(req.getBookId());
        if (book.isEmpty()) {
            return new ServiceResponse<>("9002", "Book not found");
        }

        List<Reservation> existingReservations = reservationRepository.findByUserIdAndBookId(foundUser.getId(), req.getBookId());

        boolean hasActiveReservation = existingReservations.stream()
                .anyMatch(r -> r.getStatus() == Status.WAITING);

        if (hasActiveReservation) {
            return new ServiceResponse<>("9003", "User already has a pending reservation for this book");
        }

        List<Loan> existingLoans = loanRepository.findByUserIdAndBookId(foundUser.getId(), req.getBookId());

        boolean hasActiveLoan = existingLoans.stream()
                .anyMatch(l -> l.getStatus() == Status.WAITING || l.getStatus() == Status.IN_PROGRESS);

        if (hasActiveLoan) {
            return new ServiceResponse<>("9004", "User already has an active loan for this book");
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

        return new ServiceResponse<>(
                "9000",
                "Reservation successfully created",
                toResponse(savedReservation)
        );
    }

    public ServiceResponse<?> getMyReservations(String email) {
        User foundUser = userService.findByEmail(email);

        List<Reservation> reservations = reservationRepository.findByUserId(foundUser.getId());

        List<ReservationResponse> responses = reservations.stream()
                .map(this::toResponse)
                .toList();

        return new ServiceResponse<>("9020", "Reservations retrieved successfully", responses);
    }

    public ServiceResponse<?> deleteReservation(int reservationId) {
        // Trouver la réservation :
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if (reservation.isEmpty()) {
            return new ServiceResponse<>("9011", "Reservation not found");
        }

        if (reservation.get().getStatus() != Status.WAITING) {
            return new ServiceResponse<>("9012", "Only reservation with a waiting status can be deleted");
        }

        reservationRepository.deleteById(reservationId);

        return new ServiceResponse<>("9010", "Reservation successfully deleted");
    }

    public ServiceResponse<List<?>> getReservationsByBookId(int bookId) {
        List<Reservation> reservations = reservationRepository.findByBookId(bookId);

        if (reservations.isEmpty()) {
            return new ServiceResponse<>("9020", "No reservations found");
        }

        return new ServiceResponse<>(
                "9021",
                "Reservations successfully retrieved",
                reservations.stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    public ServiceResponse<List<?>> getReservationsByUserIdAndBookId(int userId, int bookId) {
        List<Reservation> reservations = reservationRepository.findByUserIdAndBookId(userId, bookId);

        if (reservations.isEmpty()) {
            return new ServiceResponse<>("9030", "No reservations found");
        }

        return new ServiceResponse<>(
                "9031",
                "Reservations found",
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
package fr.bookhub.service;

import fr.bookhub.dto.ReservationBookResponse;
import fr.bookhub.dto.ReservationCreateRequest;
import fr.bookhub.dto.ReservationResponse;
import fr.bookhub.entity.Book;
import fr.bookhub.entity.Reservation;
import fr.bookhub.entity.Status;
import fr.bookhub.entity.User;
import fr.bookhub.repository.BookRepository;
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

    public ServiceResponse<?> createReservation(ReservationCreateRequest req) {
        ServiceResponse<User> responseUser = userService.getUserById(req.getUserId());

        if (responseUser.getCode().equals("8001")) {
            return responseUser;
        }

        User foundUser = responseUser.getData();

        // Max 5 réservations / utilisateur
        List<Reservation> userReservations = reservationRepository.findByUserId(foundUser.getId());
        if (userReservations.size() >= 5) {
            return new ServiceResponse<>("9001", "Reservation quota reached");
        }

        Optional<Book> book = bookRepository.findById(req.getBookId());
        if (book.isEmpty()) {
            return new ServiceResponse<>("9002", "Book not found");
        }

        Book foundBook = book.get();

        Reservation reservation = new Reservation();
        reservation.setBook(foundBook);
        reservation.setUser(foundUser);
        reservation.setStatus(Status.WAITING);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        int queuePosition = calculateQueuePosition(savedReservation);

        return new ServiceResponse<>(
                "9000",
                "Reservation successfully created",
                toResponse(savedReservation, queuePosition)
        );
    }

    /**
     * Mes réservations : utilise le user connecté (email extrait du JWT).
     */
    public ServiceResponse<?> getMyReservations(String email) {
        User foundUser = userService.findByEmail(email);

        List<Reservation> reservations = reservationRepository.findByUserId(foundUser.getId());

        List<ReservationResponse> responses = reservations.stream()
                .map(reservation -> toResponse(reservation, calculateQueuePosition(reservation)))
                .toList();

        return new ServiceResponse<>("9020", "Reservations retrieved successfully", responses);
    }

    /**
     * Annulation d'une réservation (DELETE /api/reservations/{id})
     */
    public ServiceResponse<?> deleteReservation(int reservationId) {
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

    /**
     * Calcule la position dans la file d'attente pour ce livre.
     *  - pour les réservations en WAITING
     *  sinon on renvoie 0 pour éviter des rangs incohérents
     */
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

    /**
     * Mapping entité vers DTO de réponse.
     */
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
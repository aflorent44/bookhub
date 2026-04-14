package fr.bookhub.service;

import fr.bookhub.entity.Book;
import fr.bookhub.entity.Reservation;
import fr.bookhub.entity.Status;
import fr.bookhub.entity.User;
import fr.bookhub.repository.BookRepository;
import fr.bookhub.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {

    private final UserService userService;
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final ReservationMapper reservationMapper;

    public ServiceResponse<?> createReservation(ReservationCreateRequest req) {
        // Récupérer l'utilisateur :
        ServiceResponse<User> responseUser = userService.getUserById(req.getUserId());

        if (responseUser.getCode().equals("8001")) {
            return responseUser;
        }

        User foundUser = responseUser.getData();

        // Vérifier le nombre de réservation de l'utilisateur :
        List<Reservation> userReservations = reservationRepository.findByUserId(req.getUserId());

        if (userReservations.size() >= 5) {
            return new ServiceResponse<Reservation>("9001","Reservation quota reached");
        }

        // Récupérer le livre :
        Optional<Book> book = bookRepository.findById(req.getBookId());
        Book foundBook;

        if (book.isPresent()) {
            foundBook = book.get();
        } else {
            return new ServiceResponse<>("9002", "Book not found");
        }

        // Création de la réservation :
        Reservation reservation = new Reservation();
        reservation.setBook(foundBook);
        reservation.setStatus(Status.WAITING);
        reservation.setUser(foundUser);
        reservation.setCreatedAt(LocalDateTime.now());

        // Sauvegarde de la réservation :
        Reservation savedReservation = reservationRepository.save(reservation);

        return new ServiceResponse<>("9000","Reservation successfully created", reservationMapper.toResponse(savedReservation));
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

        return new ServiceResponse<>("9010","Reservation successfully deleted");
    }
}

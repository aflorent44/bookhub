package fr.bookhub.controller;

import fr.bookhub.service.ReservationCreateRequest;
import fr.bookhub.service.ReservationService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/api/reservation")
    public ServiceResponse<?> reservationBook(@RequestBody ReservationCreateRequest req) {
        return reservationService.createReservation(req);
    }

    @PostMapping("/api/reservation/delete/{id}")
    public ServiceResponse<?> cancelReservation(@PathVariable int id) {
        return reservationService.deleteReservation(id);
    }

    @GetMapping("/api/reservation/book/{bookId}")
    public ServiceResponse<?> getReservationsByBookId(@PathVariable int bookId) {
        return reservationService.getReservationsByBookId(bookId);
    }

    @GetMapping("/api/reservation/user/{userId}/book/{bookId}")
    public ServiceResponse<?> getReservationsByUserIdAndBookId(@PathVariable int userId, @PathVariable int bookId) {
        return reservationService.getReservationsByUserIdAndBookId(userId, bookId);
    }
}

package fr.bookhub.controller;

import fr.bookhub.service.ReservationCreateRequest;
import fr.bookhub.service.ReservationService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("")
    public ServiceResponse<?> reservationBook(@RequestBody ReservationCreateRequest req) {
        return reservationService.createReservation(req);
    }

    @PostMapping("/delete/{id}")
    public ServiceResponse<?> cancelReservation(@PathVariable int id) {
        return reservationService.deleteReservation(id);
    }

    @GetMapping("/book/{bookId}")
    public ServiceResponse<?> getReservationsByBookId(@PathVariable int bookId) {
        return reservationService.getReservationsByBookId(bookId);
    }

    @GetMapping("/user/{userId}/book/{bookId}")
    public ServiceResponse<?> getReservationsByUserIdAndBookId(@PathVariable int userId, @PathVariable int bookId) {
        return reservationService.getReservationsByUserIdAndBookId(userId, bookId);
    }
}

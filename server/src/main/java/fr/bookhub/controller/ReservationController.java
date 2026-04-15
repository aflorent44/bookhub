package fr.bookhub.controller;

import fr.bookhub.dto.ReservationCreateRequest;
import fr.bookhub.service.ReservationService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ServiceResponse<?> reserveBook(@RequestBody ReservationCreateRequest req) {
        return reservationService.createReservation(req);
    }

    @GetMapping("/my")
    public ServiceResponse<?> getMyReservations(Authentication authentication) {
        return reservationService.getMyReservations(authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ServiceResponse<?> cancelReservation(@PathVariable int id) {
        return reservationService.deleteReservation(id);
    }
}
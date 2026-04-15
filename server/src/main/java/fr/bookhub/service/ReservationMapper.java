package fr.bookhub.service;

import fr.bookhub.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponse toResponse(Reservation reservation) {
        ReservationResponse dto = new ReservationResponse();

        dto.setId(reservation.getId());
        dto.setBookId(reservation.getBook().getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setStatus(reservation.getStatus());
        dto.setCreatedAt(reservation.getCreatedAt());

        if (reservation.getUpdatedBy() != null) {
            dto.setUpdatedBy(reservation.getUpdatedBy().getId());
            dto.setUpdatedAt(reservation.getUpdatedAt());
        }

        return dto;
    }
}

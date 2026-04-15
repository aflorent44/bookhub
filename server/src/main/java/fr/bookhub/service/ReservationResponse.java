package fr.bookhub.service;

import fr.bookhub.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer updatedBy;
}

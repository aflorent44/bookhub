package fr.bookhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@NoArgsConstructor @AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Integer id;

    @ManyToOne
    @Getter @Setter
    private User user;

    @ManyToOne
    @Getter @Setter
    private Book book;

    @Getter @Setter
    private Status status;

    @Getter @Setter
    private LocalDate createdAt;

    @Getter @Setter
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    @Getter @Setter
    private User updatedBy;
}
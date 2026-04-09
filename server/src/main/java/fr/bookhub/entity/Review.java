package fr.bookhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@NoArgsConstructor @AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Integer id;

    @ManyToOne
    @Getter @Setter
    private Book book;

    @ManyToOne
    @Getter @Setter
    private User user;

    @Getter @Setter
    private Integer rating;

    @Lob
    @Getter @Setter
    private String comment;

    @Getter @Setter
    private Boolean isHidden = false;

    @ManyToOne
    @JoinColumn(name = "hidden_by")
    @Getter @Setter
    private User hiddenBy;

    @Getter @Setter
    private LocalDate createdAt;

    @Getter @Setter
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    @Getter @Setter
    private User updatedBy;
}
package fr.bookhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @NotNull
    private Book book;

    @ManyToOne
    @NotNull
    private User user;
    private Integer rating;

    @Lob
    private String comment;

    private Boolean isHidden = false;

    @ManyToOne
    @JoinColumn(name = "hidden_by")
    private User hiddenBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;
}
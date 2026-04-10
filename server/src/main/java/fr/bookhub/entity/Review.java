package fr.bookhub.entity;

import jakarta.persistence.*;
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
    private Book book;

    @ManyToOne
    private User user;

    private Integer rating;

    @Lob
    private String comment;

    @Column(name = "is_hidden")
    private Boolean isHidden = false;

    @ManyToOne
    @JoinColumn(name = "hidden_by")
    private User hiddenBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;
}
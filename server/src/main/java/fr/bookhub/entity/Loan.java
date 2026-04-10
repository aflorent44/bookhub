package fr.bookhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import fr.bookhub.entity.Book;

@Entity
@Table(name = "loans")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "start_date")
    private LocalDateTime debutDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private Book book;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;
}

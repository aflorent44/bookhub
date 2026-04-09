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
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter @Getter
    private Integer id;

    @Setter @Getter
    private LocalDateTime debut_date;

    @Getter @Setter
    private LocalDateTime end_date;

    @Getter @Setter
    private LocalDateTime return_date;

    @Getter @Setter
    private Status status;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @Getter @Setter
    private Book book;

    @Getter @Setter
    private LocalDateTime created_at;

    @Getter @Setter
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    @Getter @Setter
    private User updated_by;
}

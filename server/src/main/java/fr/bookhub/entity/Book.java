package fr.bookhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@NoArgsConstructor @AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Integer id;

    @Getter @Setter
    private String title;

    @Getter @Setter
    private String firstPageUrl;

    @Getter @Setter
    private Integer year;

    @Column(unique = true)
    @Getter @Setter
    private String isbn;

    @Getter @Setter
    private Integer quantity = 1;

    @ManyToOne
    @JoinColumn(name = "country_code")
    @Getter @Setter
    private Country country;

    @Lob
    @Getter @Setter
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @Getter @Setter
    private Author author;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @Getter @Setter
    private Publisher publisher;

    @ManyToMany
    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Getter @Setter
    private Set<Genre> genres = new HashSet<>();

    @Getter @Setter
    private LocalDateTime createdAt;

    @Getter @Setter
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @Getter @Setter
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    @Getter @Setter
    private User updatedBy;
}
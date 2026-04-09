package fr.bookhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "authors")
@NoArgsConstructor @AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String firstName;

    @Getter @Setter
    private LocalDate birthDate;

    @Getter @Setter
    private String city;

    @ManyToOne
    @JoinColumn(name = "country_code")
    @Getter @Setter
    private Country country;

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
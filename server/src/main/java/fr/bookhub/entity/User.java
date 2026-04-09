package fr.bookhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Integer id;

    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String firstName;

    @Getter @Setter
    private String pseudo;

    @Getter @Setter
    private Boolean showRealName = false;

    @Getter @Setter
    private String phoneNumber;

    @Column(unique = true)
    @Getter @Setter
    private String email;

    @Column(nullable = false)
    @Getter @Setter
    private String userPassword;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @Getter @Setter
    private Role role;

    @Getter @Setter
    private LocalDate createdAt;

    @Getter @Setter
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @Getter @Setter
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    @Getter @Setter
    private User updatedBy;
}
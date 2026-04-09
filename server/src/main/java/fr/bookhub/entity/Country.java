package fr.bookhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countries")
@NoArgsConstructor @AllArgsConstructor
public class Country {

    @Id
    @Column(length = 3)
    @Getter @Setter
    private String code;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String nationality;
}
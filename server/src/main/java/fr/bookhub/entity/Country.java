package fr.bookhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Country {

    @Id
    @Column(length = 3)
    private String code;
    private String name;
    private String nationality;
}
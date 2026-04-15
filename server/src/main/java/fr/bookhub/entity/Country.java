package fr.bookhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Country {

    @Id
    @Column(columnDefinition = "char(3)")
    private String code;
    @NotEmpty
    private String name;
    @NotEmpty
    private String nationality;
    @Column(columnDefinition = "char(2)")
    private String language;
}
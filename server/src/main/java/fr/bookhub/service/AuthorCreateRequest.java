package fr.bookhub.service;

import fr.bookhub.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreateRequest {
    private String firstName;
    private String lastName;
    private String country;
}

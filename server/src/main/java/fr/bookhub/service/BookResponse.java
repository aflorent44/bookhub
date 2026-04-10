package fr.bookhub.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Integer id;
    private String isbn;
    private String title;

    private Integer year;
    private Integer quantity;
    private String description;

    private String authorFirstName;
    private String authorLastName;

    private String publisherName;

    private Set<GenreDTO> genres;
}
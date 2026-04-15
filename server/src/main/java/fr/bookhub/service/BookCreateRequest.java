package fr.bookhub.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateRequest {
    private Integer bookId;
    private String isbn;
    private String title;
    private Integer year;
    private Integer quantity;
    private String description;
    private String firstPageUrl;

    private String authorFirstName;
    private String authorLastName;

    private String publisherName;

    private String countryName;

    private Set<GenreDTO> genres;

    private Integer createdById;
    private Integer updatedById;
}

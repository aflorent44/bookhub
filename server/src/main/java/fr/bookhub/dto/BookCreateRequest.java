package fr.bookhub.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "ISBN requis.")
    @Size(min = 10, max = 20, message = "L'isbn doit être compris entre 10 et 20 caractères.")
    private String isbn;

    @NotBlank(message = "Titre obligatoire.")
    private String title;

    @NotNull(message = "L'année est obligatoire.")
    @Min(value = 1450, message = "L'année est invalide.")
    @Max(value = 2100, message = "L'année est invalide.")
    private Integer year;

    @Min(value = 0, message = "La quantité ne peut pas être négative.")
    private Integer quantity;

    @NotBlank(message = "La description est obligatoire.")
    private String description;

    @Pattern(
            regexp = "^(https?://.*)?$",
            message = "Url non valide."
    )
    private String firstPageUrl;

    @Size(max = 100, message = "Prénom de l'auteur trop long.")
    private String authorFirstName;

    @Size(max = 100, message = "Nom de l'auteur trop long.")
    private String authorLastName;

    @Size(max = 150, message = "Nom de l'éditeur trop long.")
    private String publisherName;

    @Size(max = 100, message = "Nom du pays trop long.")
    private String countryName;

    @NotNull(message = "Au moins un genre est obligatoire.")
    @Size(min = 1)
    private Set<@Valid GenreDTO> genres;

    private Integer createdById;
    private Integer updatedById;
}

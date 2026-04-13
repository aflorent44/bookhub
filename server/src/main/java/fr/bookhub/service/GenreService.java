package fr.bookhub.service;

import fr.bookhub.entity.Genre;
import fr.bookhub.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public ServiceResponse<Genre> createGenre(String label) {
        if (label.isEmpty()) {
            return new ServiceResponse<>("4001", "Label cannot be empty");
        }

        Genre genre = new Genre();
        genre.setLabel(label.trim());

        genreRepository.save(genre);

        return new ServiceResponse<>("4000", "Genre successfully created", genre);
    }
}

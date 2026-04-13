package fr.bookhub.service;

import fr.bookhub.entity.Genre;
import fr.bookhub.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ServiceResponse<List<GenreDTO>> getGenres() {
        if (genreRepository.findAll().isEmpty()) {
            return new ServiceResponse<>("4003", "No genres found");
        }

        List<GenreDTO> genres = genreRepository.findAll()
                .stream()
                .map(genre -> new GenreDTO(genre.getId(), genre.getLabel()))
                .toList();

        return new ServiceResponse<List<GenreDTO>>("4002", "Genres successfully found", genres);
    }
}

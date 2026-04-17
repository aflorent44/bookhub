package fr.bookhub.service;

import fr.bookhub.dto.GenreDTO;
import fr.bookhub.entity.Genre;
import fr.bookhub.repository.GenreRepository;
import fr.bookhub.utility.ApiCode;
import fr.bookhub.utility.ApiException;
import fr.bookhub.utility.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public ServiceResponse<Genre> createGenre(String label) {
        if (label.isEmpty()) {
            throw new ApiException(ApiCode.GENRE_LABEL_EMPTY);
        }

        Genre genre = new Genre();
        genre.setLabel(label.trim());

        genreRepository.save(genre);

        return new ServiceResponse<>(ApiCode.GENRE_CREATED, genre);
    }

    public ServiceResponse<List<GenreDTO>> getGenres() {
        if (genreRepository.findAll().isEmpty()) {
            throw new ApiException(ApiCode.NO_GENRES_FOUND);
        }

        List<GenreDTO> genres = genreRepository.findAll()
                .stream()
                .map(genre -> new GenreDTO(genre.getId(), genre.getLabel()))
                .toList();

        return new ServiceResponse<>(ApiCode.GENRES_FOUND, genres);
    }
}

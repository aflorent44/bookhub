package fr.bookhub.controller;

import fr.bookhub.dto.GenreDTO;
import fr.bookhub.entity.Genre;
import fr.bookhub.service.GenreService;
import fr.bookhub.utility.ServiceResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public ServiceResponse<List<GenreDTO>> getGenres() {
        return genreService.getGenres();
    }

    @PostMapping
    public ServiceResponse<Genre> createGenre(@RequestBody @Valid GenreDTO genreDTO) {
        return genreService.createGenre(genreDTO.getLabel());
    }
}
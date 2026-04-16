package fr.bookhub.controller;

import fr.bookhub.dto.GenreDTO;
import fr.bookhub.service.GenreService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping("")
    public ServiceResponse<List<GenreDTO>> getGenres() {
        return genreService.getGenres();
    }

}
package fr.bookhub.controller;

import fr.bookhub.dto.AuthorCreateRequest;
import fr.bookhub.service.AuthorService;
import fr.bookhub.service.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/api/add-author")
    public ServiceResponse<?> addAuthor(@RequestBody AuthorCreateRequest authorRequest) {
        return authorService.createAuthor(authorRequest);
    }
}

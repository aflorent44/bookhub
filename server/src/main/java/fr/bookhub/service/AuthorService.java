package fr.bookhub.service;

import fr.bookhub.entity.Author;
import fr.bookhub.entity.Country;
import fr.bookhub.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public ServiceResponse<Author> createAuthor(String firstName, String lastName, Country country) {
        if (firstName.isEmpty()) return new ServiceResponse<>("3001", "First name is empty");
        if (lastName.isEmpty()) return new ServiceResponse<>("3002", "Last name is empty");
        if (country == null) return new ServiceResponse<>("3003", "Country is empty");

        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setCountry(country);
        author.setCreatedAt(LocalDateTime.now());
        author.setUpdatedAt(LocalDateTime.now());

        authorRepository.save(author);

        return new ServiceResponse<>("3000", "Author successfully created", author);
    }
}

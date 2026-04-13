package fr.bookhub.service;

import fr.bookhub.entity.Author;
import fr.bookhub.entity.Country;
import fr.bookhub.repository.AuthorRepository;
import fr.bookhub.repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final CountryRepository countryRepository;

    public ServiceResponse<Author> createAuthor(AuthorCreateRequest req) {
        if (req.getFirstName().isEmpty()) return new ServiceResponse<>("3001", "First name is empty");
        if (req.getLastName().isEmpty()) return new ServiceResponse<>("3002", "Last name is empty");
        if (req.getCountry() == null) return new ServiceResponse<>("3003", "Country is empty");

        System.out.println("Reçu: [" + req.getCountry() + "]");
        System.out.println("Longueur: " + req.getCountry().length());
        System.out.println("Bytes: " + java.util.Arrays.toString(req.getCountry().getBytes()));

        Author author = new Author();
        author.setFirstName(req.getFirstName());
        author.setLastName(req.getLastName());

        Optional<Country> country = countryRepository.findByNameIgnoreCase(req.getCountry());

        if (country.isPresent()) {
            System.out.println("Country existente" + country.get());
            author.setCountry(country.get());
        } else {
            System.out.println("Country non existente");
        }

        author.setCountry(
                countryRepository.findByNameIgnoreCase(req.getCountry())
                        .orElseGet(() -> countryRepository.findByNameIgnoreCase("United States")
                                .orElseThrow(() -> new RuntimeException("Default country USA not found"))
                        )
        );

        author.setCreatedAt(LocalDateTime.now());
        author.setUpdatedAt(LocalDateTime.now());

        authorRepository.save(author);

        return new ServiceResponse<>("3000", "Author successfully created", author);
    }
}

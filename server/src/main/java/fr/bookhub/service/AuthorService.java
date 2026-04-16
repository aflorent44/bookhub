package fr.bookhub.service;

import fr.bookhub.dto.author.AuthorCreateRequest;
import fr.bookhub.entity.Author;
import fr.bookhub.repository.AuthorRepository;
import fr.bookhub.repository.CountryRepository;
import fr.bookhub.utility.ApiCode;
import fr.bookhub.utility.ApiException;
import fr.bookhub.utility.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final CountryRepository countryRepository;

    public ServiceResponse<Author> createAuthor(AuthorCreateRequest req) {
        if (req.firstName().isEmpty()) throw new ApiException(ApiCode.AUTHOR_FIRSTNAME_EMPTY);
        if (req.lastName().isEmpty()) throw new ApiException(ApiCode.AUTHOR_LASTNAME_EMPTY);
        if (req.country() == null) throw new ApiException(ApiCode.AUTHOR_COUNTRY_EMPTY);

        Author author = new Author();
        author.setFirstName(req.firstName());
        author.setLastName(req.lastName());

        author.setCountry(
                countryRepository.findByNameIgnoreCase(req.country())
                        .orElseGet(() -> countryRepository.findByNameIgnoreCase("United States")
                                .orElseThrow(() -> new RuntimeException("Default country USA not found"))
                        )
        );

        author.setCreatedAt(LocalDateTime.now());
        author.setUpdatedAt(LocalDateTime.now());

        authorRepository.save(author);

        return new ServiceResponse<>(ApiCode.AUTHOR_CREATED, author);
    }
}

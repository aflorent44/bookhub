package fr.bookhub.service;

import fr.bookhub.entity.*;
import fr.bookhub.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;
    private final CountryRepository countryRepository;

    private final BookMapper bookMapper;
    private final PublisherService publisherService;
    private final GenreService genreService;
    private final AuthorService authorService;

    public ServiceResponse<List<BookResponse>> getBooks() {
        List<BookResponse> books = bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponse)
                .toList();

        if (books.isEmpty()) {
            return new ServiceResponse<>("1001", "Books not found");
        }

        return new ServiceResponse<>("1000", "Books found",  books);
    }

    public ServiceResponse<BookResponse> getBookById(Integer id) {
        return bookRepository.findById(id)
                .map(book -> {
                    BookResponse response = bookMapper.toResponse(book);
                    return new ServiceResponse<>("1010", "Book found", response);
                })
                .orElseGet(() ->
                        new ServiceResponse<>("1011", "Book not found")
                );
    }

    public ServiceResponse<BookResponse> createBook(BookCreateRequest req) {

        // 1. validation simple
        ServiceResponse<BookCreateRequest> validation = validate(req);
        if (!"1020".equals(validation.getCode())) {
            return new ServiceResponse<>(validation.getCode(), validation.getMessage());
        }

        // 2. check ISBN
        if (bookRepository.findBookByIsbn(req.getIsbn()).isPresent()) {
            return new ServiceResponse<>("1031", "Book already exists");
        }

        // 3. entity
        Book book = new Book();
        book.setTitle(req.getTitle());
        book.setIsbn(req.getIsbn());
        book.setYear(req.getYear());
        book.setQuantity(req.getQuantity());
        book.setDescription(req.getDescription());
        book.setFirstPageUrl(req.getFirstPageUrl());
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        // 4. relations
        Country country = (req.getCountryName() != null)
                ? countryRepository.findByName(req.getCountryName())
                .orElseGet(() ->
                        countryRepository.findById("USA")
                                .orElseThrow(() -> new RuntimeException("Default country USA not found"))
                )
                : countryRepository.findById("USA")
                .orElseThrow(() -> new RuntimeException("Default country USA not found"));

        book.setCountry(country);

        book.setPublisher(
                publisherRepository.findByName(req.getPublisherName())
                        .orElseGet(() -> {
                            ServiceResponse<Publisher> publisherResponses =
                                    publisherService.createPublisher(req.getPublisherName());

                            if (!"2000".equals(publisherResponses.getCode()) || publisherResponses.getData() == null) {
                                throw new RuntimeException("Publisher creation failed");
                            }

                            return publisherResponses.getData();
                        })
        );

        book.setAuthor(
                authorRepository.findByLastName(req.getAuthorLastName())
                        .orElseGet(() -> {
                            ServiceResponse<Author> authorResponse =
                                    authorService.createAuthor(req.getAuthorFirstName(), req.getAuthorLastName(), book.getCountry());

                            if (!"3001".equals(authorResponse.getCode()) || authorResponse.getData() == null) {
                                throw new RuntimeException("Author creation failed");
                            }

                            return authorResponse.getData();
                        })
        );

        // 5. genres
        Set<Genre> genres = req.getGenres().stream()
                .map(genre -> genreRepository.findByLabel(genre.getLabel())
                        .orElseGet(() -> {
                            ServiceResponse<Genre> genreResponse =
                                    genreService.createGenre(genre.getLabel());

                            if (!"2001".equals(genreResponse.getCode()) || genreResponse.getData() == null) {
                                throw new RuntimeException("Genre creation failed");
                            }

                            return genreResponse.getData();
                        }))
                .collect(Collectors.toSet());

        book.setGenres(genres);

        // 6. save
        Book saved = bookRepository.save(book);

        return new ServiceResponse<>("1030", "Book saved", bookMapper.toResponse(saved)); // Succeed
    }

    private ServiceResponse<BookCreateRequest> validate(BookCreateRequest req) {
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            return new ServiceResponse<>("1021", "Title is required");
        }

        if (req.getIsbn() == null) {
            return new ServiceResponse<>("1022", "Isbn is required");
        }

        if (req.getGenres() == null || req.getGenres().isEmpty()) {
            return new ServiceResponse<>("1023", "Genres is required");
        }

        return new ServiceResponse<>("1020", "All required fields are present");
    }
}
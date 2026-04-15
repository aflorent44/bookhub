package fr.bookhub.service;

import fr.bookhub.entity.*;
import fr.bookhub.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import fr.bookhub.service.filter.BookSearchFilter;
import fr.bookhub.service.specification.BookSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private final UserService userService;
    private final LoanRepository loanRepository;

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

    // Création ou modification d'un livre :
    public ServiceResponse<BookResponse> createOrUpdateBook(BookCreateRequest req, MethodType method) {
        // 1. validation simple
        ServiceResponse<BookCreateRequest> validation = validate(req);
        if (!"1020".equals(validation.getCode())) {
            return new ServiceResponse<>(validation.getCode(), validation.getMessage());
        }

        // 2. check ISBN
        if (bookRepository.findBookByIsbn(req.getIsbn()).isPresent() && method == MethodType.CREATE) {
            return new ServiceResponse<>("1031", "Book already exists");
        }

        User internalUser = userService.getUserById(req.getCreatedById()).getData();

        if (internalUser == null) {
            return new ServiceResponse<>("1032", "Internal user not found");
        }

        // 3. entity
        Book book;

        if (method == MethodType.CREATE) {
            book = new Book();
            book.setCreatedAt(LocalDateTime.now());
            book.setCreatedBy(internalUser);
        } else {
            book = bookRepository.findBookByIsbn(req.getIsbn()).get();
            book.setUpdatedAt(LocalDateTime.now());
            book.setUpdatedBy(internalUser);
        }

        book.setTitle(req.getTitle());
        book.setIsbn(req.getIsbn());
        book.setYear(req.getYear());
        book.setQuantity(req.getQuantity());
        book.setDescription(req.getDescription());
        book.setFirstPageUrl(req.getFirstPageUrl());

        // 4. relations
        Country country = (req.getCountryName() != null)
                ? countryRepository.findByLanguageIgnoreCase(req.getCountryName())
                .orElseGet(() ->
                        countryRepository.findByCode("USA")
                                .orElseThrow(() -> new RuntimeException("Default country USA not found"))
                )
                : countryRepository.findByCode("USA")
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
                                    authorService.createAuthor(
                                            new AuthorCreateRequest(req.getAuthorFirstName(), req.getAuthorLastName(), book.getCountry().getName()));

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

        if (method == MethodType.CREATE) return new ServiceResponse<>("1030", "Book saved", bookMapper.toResponse(saved)); // Succeed
        return new ServiceResponse<>("1033", "Book successfully updated", bookMapper.toResponse(saved));
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

    // Recherche avancée avec filtre(s) :
    public ServiceResponse<Page<BookResponse>> search(BookSearchFilter filter) {

        // 1. Build specification
        Specification<Book> spec = buildSpecification(filter);

        // 2. Pagination + tri
        Pageable pageable = buildPageable(filter);

        // 3. Query DB
        Page<Book> booksPage = bookRepository.findAll(spec, pageable);

        // 4. Mapping
        Page<BookResponse> responsePage = booksPage.map(bookMapper::toResponse);

        // 5. Result
        if (responsePage.isEmpty()) {
            return new ServiceResponse<>("5001", "No result for this search");
        }

        return new ServiceResponse<>("5000", "Books found", responsePage);
    }

    private Specification<Book> buildSpecification(BookSearchFilter filter) {

        Specification<Book> spec = Specification.unrestricted();

        if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
            spec = spec.and(BookSpecification.keyword(filter.getKeyword()));
        }

        if (filter.getTitle() != null && !filter.getTitle().isBlank()) {
            spec = spec.and(BookSpecification.title(filter.getTitle()));
        }

        if (filter.getYear() != null) {
            spec = spec.and(BookSpecification.year(filter.getYear()));
        }

        if (filter.getIsbn() != null && !filter.getIsbn().isBlank()) {
            spec = spec.and(BookSpecification.isbn(filter.getIsbn()));
        }

        if (filter.getAuthorFirstName() != null || filter.getAuthorLastName() != null) {
            spec = spec.and(BookSpecification.author(
                    filter.getAuthorFirstName(),
                    filter.getAuthorLastName()
            ));
        }

        if (filter.getGenre() != null && !filter.getGenre().isBlank()) {
            spec = spec.and(BookSpecification.genre(filter.getGenre()));
        }

        if (filter.getPublisher() != null && !filter.getPublisher().isBlank()) {
            spec = spec.and(BookSpecification.publisher(filter.getPublisher()));
        }

        if (filter.getCountryName() != null || filter.getCountryNationality() != null) {
            spec = spec.and(BookSpecification.country(
                    filter.getCountryName(),
                    filter.getCountryNationality()
            ));
        }

        return spec;
    }

    private Pageable buildPageable(BookSearchFilter filter) {

        String sortBy = (filter.getSortBy() == null || filter.getSortBy().isBlank())
                ? "title"
                : filter.getSortBy();

        Sort.Direction direction =
                "desc".equalsIgnoreCase(filter.getSortDirection())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        int page = Math.max(filter.getPage(), 0);
        int size = filter.getSize() < 1 ? 21 : filter.getSize();

        return PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );
    }

    public ServiceResponse<?> delete(int id) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            return new ServiceResponse<>("6001", "Book not found");
        }

        // Vérifier s'il y a des emprunts sur le livre :
        List<Loan> loans = loanRepository.findByBookId(book.getId());

        for (Loan loan : loans) {
            if (loan.getStatus() == Status.IN_PROGRESS || loan.getStatus() == Status.WAITING) {
                return new ServiceResponse<>("6002", "A loan is in progress or a user is waiting to loan this book");
            }
        }

        return new ServiceResponse<>("6000", "Book successfully deleted", book);
    }
}
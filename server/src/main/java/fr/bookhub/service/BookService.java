package fr.bookhub.service;

import fr.bookhub.dto.author.AuthorCreateRequest;
import fr.bookhub.dto.book.BookCreateRequest;
import fr.bookhub.dto.book.BookMapper;
import fr.bookhub.dto.book.BookResponse;
import fr.bookhub.entity.*;
import fr.bookhub.repository.*;
import fr.bookhub.utility.ApiCode;
import fr.bookhub.utility.ApiException;
import fr.bookhub.utility.MethodType;
import fr.bookhub.utility.ServiceResponse;
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
            throw new ApiException(ApiCode.BOOKS_NOT_FOUND);
        }

        return new ServiceResponse<>(ApiCode.BOOKS_FOUND, books);
    }

    public ServiceResponse<BookResponse> getBookById(Integer id) {
        return bookRepository.findById(id)
                .map(book -> {
                    BookResponse response = bookMapper.toResponse(book);
                    return new ServiceResponse<>(ApiCode.BOOK_FOUND, response);
                })
                .orElseThrow(() -> new ApiException(ApiCode.BOOK_NOT_FOUND));
    }

    // Création ou modification d'un livre :
    public ServiceResponse<BookResponse> createOrUpdateBook(BookCreateRequest req, MethodType method) {
        // 1. validation simple
        validate(req);

        // 2. check ISBN
        if (bookRepository.findBookByIsbn(req.isbn()).isPresent() && method == MethodType.CREATE) {
            throw new ApiException(ApiCode.BOOK_ALREADY_EXISTS);
        }

        User internalUser = userService.getUserById(req.createdById()).getData();

        if (internalUser == null) {
            throw new ApiException(ApiCode.INTERNAL_USER_NOT_FOUND);
        }

        // 3. entity
        Book book;

        if (method == MethodType.CREATE) {
            book = new Book();
            book.setCreatedAt(LocalDateTime.now());
            book.setCreatedBy(internalUser);
        } else {
            book = bookRepository.findBookByIsbn(req.isbn()).get();
            book.setUpdatedAt(LocalDateTime.now());
            book.setUpdatedBy(internalUser);
        }

        book.setTitle(req.title());
        book.setIsbn(req.isbn());
        book.setYear(req.year());
        book.setQuantity(req.quantity());
        book.setDescription(req.description());
        book.setFirstPageUrl(req.firstPageUrl());

        // 4. relations
        Country country = (req.countryName() != null)
                ? countryRepository.findByLanguageIgnoreCase(req.countryName())
                .orElseGet(() ->
                        countryRepository.findByCode("USA")
                                .orElseThrow(() -> new RuntimeException("Default country USA not found"))
                )
                : countryRepository.findByCode("USA")
                .orElseThrow(() -> new RuntimeException("Default country USA not found"));

        book.setCountry(country);

        book.setPublisher(
                publisherRepository.findByName(req.publisherName())
                        .orElseGet(() -> {
                            ServiceResponse<Publisher> publisherResponses =
                                    publisherService.createPublisher(req.publisherName());

                            if (!"2000".equals(publisherResponses.getCode()) || publisherResponses.getData() == null) {
                                throw new RuntimeException("Publisher creation failed");
                            }

                            return publisherResponses.getData();
                        })
        );

        book.setAuthor(
                authorRepository.findByLastName(req.authorLastName())
                        .orElseGet(() -> {
                            ServiceResponse<Author> authorResponse =
                                    authorService.createAuthor(
                                            new AuthorCreateRequest(req.authorFirstName(), req.authorLastName(), book.getCountry().getName()));

                            if (!"3001".equals(authorResponse.getCode()) || authorResponse.getData() == null) {
                                throw new RuntimeException("Author creation failed");
                            }

                            return authorResponse.getData();
                        })
        );

        // 5. genres
        Set<Genre> genres = req.genres().stream()
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

        if (method == MethodType.CREATE) return new ServiceResponse<>(ApiCode.BOOK_CREATED, bookMapper.toResponse(saved)); // Succeed
        return new ServiceResponse<>(ApiCode.BOOK_UPDATED, bookMapper.toResponse(saved));
    }

    private ServiceResponse<BookCreateRequest> validate(BookCreateRequest req) {
        if (req.title() == null || req.title().isBlank()) {
            throw new ApiException(ApiCode.TITLE_REQUIRED);
        }

        if (req.isbn() == null) {
            throw new ApiException(ApiCode.ISBN_REQUIRED);
        }

        if (req.genres() == null || req.genres().isEmpty()) {
            throw new ApiException(ApiCode.GENRES_REQUIRED);
        }

        return new ServiceResponse<>(ApiCode.ALL_FIELDS_VALID);
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
            return new ServiceResponse<>(ApiCode.SEARCH_RESULTS_FOUND, responsePage);
        }

        return new ServiceResponse<>(ApiCode.SEARCH_RESULTS_FOUND, responsePage);
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
        int size = Math.max(filter.getSize(), 0);

        return PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );
    }

    public ServiceResponse<?> delete(int id) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            throw new ApiException(ApiCode.BOOK_DELETE_NOT_FOUND);
        }

        // Vérifier s'il y a des emprunts sur le livre :
        List<Loan> loans = loanRepository.findByBookId(book.getId());

        for (Loan loan : loans) {
            if (loan.getStatus() == Status.IN_PROGRESS || loan.getStatus() == Status.WAITING) {
                throw new ApiException(ApiCode.BOOK_DELETE_CONFLICT);
            }
        }

        return new ServiceResponse<>(ApiCode.BOOK_DELETED, book);
    }
}
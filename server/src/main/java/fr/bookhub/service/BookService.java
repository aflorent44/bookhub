package fr.bookhub.service;

import fr.bookhub.dto.AuthorCreateRequest;
import fr.bookhub.dto.BookCreateRequest;
import fr.bookhub.dto.BookMapper;
import fr.bookhub.dto.BookResponse;
import fr.bookhub.entity.*;
import fr.bookhub.repository.*;
import fr.bookhub.service.filter.BookSearchFilter;
import fr.bookhub.service.specification.BookSpecification;
import fr.bookhub.utility.ApiCode;
import fr.bookhub.utility.ApiException;
import fr.bookhub.utility.MethodType;
import fr.bookhub.utility.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        if (bookRepository.findBookByIsbn(req.getIsbn()).isPresent() && method == MethodType.CREATE) {
            throw new ApiException(ApiCode.BOOK_ALREADY_EXISTS);
        }

        User internalUser = userService.getUserById(req.getCreatedById()).getData();

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

        if (req.getPublisherName() != null && !req.getPublisherName().isBlank()) {
            book.setPublisher(
                    publisherRepository.findByName(req.getPublisherName().trim())
                            .orElseGet(() -> {
                                ServiceResponse<Publisher> publisherResponse =
                                        publisherService.createPublisher(req.getPublisherName().trim());

                                if (publisherResponse.getData() == null) {
                                    throw new RuntimeException("Publisher creation failed");
                                }

                                return publisherResponse.getData();
                            })
            );
        } else {
            book.setPublisher(null);
        }

        book.setAuthor(
                authorRepository.findByLastName(req.getAuthorLastName())
                        .orElseGet(() -> {
                            ServiceResponse<Author> authorResponse =
                                    authorService.createAuthor(
                                            new AuthorCreateRequest(req.getAuthorFirstName(), req.getAuthorLastName(), book.getCountry().getName()));

                            if (authorResponse.getData() == null) {
                                throw new RuntimeException("Author creation failed: " + authorResponse.getCode());
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

                            if (genreResponse.getData() == null) {
                                throw new RuntimeException("Genre creation failed");
                            }

                            return genreResponse.getData();
                        }))
                .collect(Collectors.toSet());

        // 6. save
        Book saved = bookRepository.save(book);

        if (method == MethodType.CREATE) return new ServiceResponse<>(ApiCode.BOOK_CREATED, bookMapper.toResponse(saved)); // Succeed
        return new ServiceResponse<>(ApiCode.BOOK_UPDATED, bookMapper.toResponse(saved));
    }

    private ServiceResponse<BookCreateRequest> validate(BookCreateRequest req) {
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new ApiException(ApiCode.TITLE_REQUIRED);
        }

        if (req.getIsbn() == null) {
            throw new ApiException(ApiCode.ISBN_REQUIRED);
        }

        if (req.getGenres() == null || req.getGenres().isEmpty()) {
            throw new ApiException(ApiCode.GENRES_REQUIRED);
        }

        return new ServiceResponse<>(ApiCode.ALL_FIELDS_VALID);
    }

//    // Recherche avancée avec filtre(s) :
//    public ServiceResponse<Page<BookResponse>> search(BookSearchFilter filter) {
//
//        // 1. Build specification
//        Specification<Book> spec = buildSpecification(filter);
//
//        // 2. Pagination + tri
//        Pageable pageable = buildPageable(filter);
//
//        // 3. Query DB
//        Page<Book> booksPage = bookRepository.findAll(spec, pageable);
//
//        // 4. Mapping
//        Page<BookResponse> responsePage = booksPage.map(bookMapper::toResponse);
//
//        // 5. Result
//        if (responsePage.isEmpty()) {
//            return new ServiceResponse<>(ApiCode.SEARCH_RESULTS_FOUND, responsePage);
//            }
//
//        return new ServiceResponse<>(ApiCode.SEARCH_RESULTS_FOUND, responsePage);
//    }

    public ServiceResponse<?> search(BookSearchFilter filter) {
        Specification<Book> spec = buildSpecification(filter);
        Pageable pageable = buildPageable(filter);
        Page<Book> booksPage = bookRepository.findAll(spec, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("content", booksPage.getContent().stream().map(bookMapper::toResponse).toList());
        result.put("totalElements", booksPage.getTotalElements());
        result.put("totalPages", booksPage.getTotalPages());
        result.put("size", booksPage.getSize());
        result.put("number", booksPage.getNumber());

        return new ServiceResponse<>(ApiCode.SEARCH_RESULTS_FOUND, result);
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
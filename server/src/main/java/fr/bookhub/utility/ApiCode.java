package fr.bookhub.utility;

import org.springframework.http.HttpStatus;

public enum ApiCode {

    // =======================
    // BOOK (1000 - 1099)
    // =======================
    BOOKS_FOUND("1000", "Books found", HttpStatus.OK),
    BOOKS_NOT_FOUND("1001", "Books not found", HttpStatus.NO_CONTENT),

    BOOK_FOUND("1010", "Book found", HttpStatus.OK),
    BOOK_NOT_FOUND("1011", "Book not found", HttpStatus.NOT_FOUND),

    ALL_FIELDS_VALID("1020", "All required fields are present", HttpStatus.OK),
    TITLE_REQUIRED("1021", "Title is required", HttpStatus.BAD_REQUEST),
    ISBN_REQUIRED("1022", "Isbn is required", HttpStatus.BAD_REQUEST),
    GENRES_REQUIRED("1023", "Genres is required", HttpStatus.BAD_REQUEST),

    BOOK_CREATED("1030", "Book saved", HttpStatus.CREATED),
    BOOK_ALREADY_EXISTS("1031", "Book already exists", HttpStatus.CONFLICT),
    INTERNAL_USER_NOT_FOUND("1032", "Internal user not found", HttpStatus.NOT_FOUND),
    BOOK_UPDATED("1033", "Book successfully updated", HttpStatus.OK),

    // =======================
    // PUBLISHER (2000 - 2099)
    // =======================
    PUBLISHER_CREATED("2000", "Publisher successfully created", HttpStatus.CREATED),
    PUBLISHER_NAME_EMPTY("2001", "Publisher name can't be empty", HttpStatus.BAD_REQUEST),

    // =======================
    // AUTHOR (3000 - 3099)
    // =======================
    AUTHOR_CREATED("3000", "Author successfully created", HttpStatus.CREATED),
    AUTHOR_FIRSTNAME_EMPTY("3001", "First name is empty", HttpStatus.BAD_REQUEST),
    AUTHOR_LASTNAME_EMPTY("3002", "Last name is empty", HttpStatus.BAD_REQUEST),
    AUTHOR_COUNTRY_EMPTY("3003", "Country is empty", HttpStatus.BAD_REQUEST),

    // =======================
    // GENRE (4000 - 4099)
    // =======================
    GENRE_CREATED("4000", "Genre successfully created", HttpStatus.CREATED),
    GENRE_LABEL_EMPTY("4001", "Label cannot be empty", HttpStatus.BAD_REQUEST),
    GENRES_FOUND("4002", "Genres successfully found", HttpStatus.OK),
    NO_GENRES_FOUND("4003", "No genres found", HttpStatus.NO_CONTENT),

    // =======================
    // SEARCH (5000 - 5099)
    // =======================
    SEARCH_RESULTS_FOUND("5000", "Books found", HttpStatus.OK),
    SEARCH_NO_RESULTS("5001", "No result for this search", HttpStatus.NO_CONTENT),

    // =======================
    // BOOK DELETE (6000 - 6099)
    // =======================
    BOOK_DELETED("6000", "Book successfully deleted", HttpStatus.OK),
    BOOK_DELETE_NOT_FOUND("6001", "Book not found", HttpStatus.NOT_FOUND),
    BOOK_DELETE_CONFLICT("6002", "A loan is in progress or a user is waiting to loan this book", HttpStatus.CONFLICT),

    // =======================
    // LOAN (7000 - 7099)
    // =======================
    LOAN_CREATED("7000", "Loan successfully created", HttpStatus.CREATED),
    LOAN_BOOK_NOT_AVAILABLE("7001", "Book not available", HttpStatus.CONFLICT),
    LOAN_BOOK_NOT_FOUND("7002", "Book not found", HttpStatus.NOT_FOUND),
    LOAN_LATE_EXISTS("7003", "One or more books are late", HttpStatus.CONFLICT),
    LOAN_QUOTA_REACHED("7004", "Loan quota reached", HttpStatus.CONFLICT),
    LOAN_ALREADY_EXISTS_FOR_BOOK("7005", "Loan quota reached for this book", HttpStatus.CONFLICT),

    LOAN_RETURNED("7010", "Book successfully returned", HttpStatus.OK),
    LOAN_NOT_FOUND("7011", "Loan not found", HttpStatus.NOT_FOUND),
    LOAN_RETURN_BOOK_NOT_FOUND("7012", "Book not found", HttpStatus.NOT_FOUND),
    LOAN_INVALID_RETURN_STATUS("7013", "User can't return the loan because the status is not in progress", HttpStatus.CONFLICT),
    LOAN_INVALID_CANCEL_STATUS("7014", "User can't cancel the loan because the status is not waiting", HttpStatus.CONFLICT),
    LOAN_CANCELED("7015", "Loan successfully canceled", HttpStatus.OK),

    LOAN_VALIDATED("7020", "Loan successfully validated", HttpStatus.OK),
    LOAN_VALIDATE_NOT_FOUND("7021", "Loan not found", HttpStatus.NOT_FOUND),

    // =======================
    // USER (8000 - 8099)
    // =======================
    USER_FOUND("8000", "User found", HttpStatus.OK),
    USER_NOT_FOUND("8001", "User not found", HttpStatus.NOT_FOUND),

    // =======================
    // RESERVATION (9000 - 9099)
    // =======================
    RESERVATION_CREATED("9000", "Reservation successfully created", HttpStatus.CREATED),
    RESERVATION_QUOTA_REACHED("9001", "Reservation quota reached", HttpStatus.CONFLICT),
    RESERVATION_BOOK_NOT_FOUND("9002", "Book not found", HttpStatus.NOT_FOUND),
    RESERVATION_ALREADY_EXISTS("9003", "Reservation already exists for this user and this book", HttpStatus.CONFLICT),
    RESERVATION_LOAN_CONFLICT("9004", "This user already has a loan for this book", HttpStatus.CONFLICT),

    RESERVATION_DELETED("9010", "Reservation successfully deleted", HttpStatus.NO_CONTENT),
    RESERVATION_NOT_FOUND("9011", "Reservation not found", HttpStatus.NOT_FOUND),
    RESERVATION_INVALID_STATUS("9012", "Only reservation with a waiting status can be deleted", HttpStatus.CONFLICT),
    RESERVATION_RETRIEVES("9020", "Reservations retrieved successfully",  HttpStatus.OK),

    RESERVATION_UNAUTHORIZED("9021", "Unauthorized", HttpStatus.UNAUTHORIZED),

    // =======================
    // REVIEW (10000 - 10099)
    // =======================
    REVIEW_CREATED("10000", "Review successfully created", HttpStatus.CREATED),
    REVIEWS_RETRIEVED("10012", "Reviews successfully retrieved", HttpStatus.OK),
    REVIEW_BOOK_NOT_FOUND("10001", "Book not found", HttpStatus.NOT_FOUND),
    REVIEW_INVALID_RATING("10002", "Invalid rating", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND("10003", "Review not found", HttpStatus.NOT_FOUND),
    REVIEW_USER_UNAUTHORIZED("10004", "User unauthorized to review", HttpStatus.FORBIDDEN),
    REVIEW_UPDATED("10005", "Review successfully updated", HttpStatus.OK),
    REVIEW_ALREADY_EXISTS("10006", "User unauthorized to review", HttpStatus.CONFLICT),

    REVIEW_DELETED("10010", "Review successfully deleted", HttpStatus.NO_CONTENT),
    REVIEW_DELETE_NOT_FOUND("10011", "Review not found", HttpStatus.NOT_FOUND),

    // =======================
    // DASHBOARD / ROLE (11000 - 11099)
    // =======================
    ROLE_UPDATED("11000", "User role successfully updated", HttpStatus.OK),
    ROLE_FORBIDDEN("11001", "Only admin is authorized to update user role", HttpStatus.FORBIDDEN),
    ROLE_INVALID("11002", "This role can't be applied to a user", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ApiCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
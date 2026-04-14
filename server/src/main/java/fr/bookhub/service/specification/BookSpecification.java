package fr.bookhub.service.specification;

import fr.bookhub.entity.Book;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class BookSpecification {

    private static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static Specification<Book> keyword(String keyword) {
        return (root, query, cb) -> {
            if (!isNotEmpty(keyword)) return null;

            query.distinct(true);

            Join<Object, Object> author = root.join("author", JoinType.LEFT);

            String like = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(root.get("isbn")), like),
                    cb.like(cb.lower(author.get("firstName")), like),
                    cb.like(cb.lower(author.get("lastName")), like)
            );
        };
    }

    public static Specification<Book> title(String title) {
        return (root, query, cb) -> {
            if (!isNotEmpty(title)) return null;

            String like = "%" + title.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("title")), like);
        };
    }

    public static Specification<Book> year(Integer year) {
        return (root, query, cb) -> {
            if (year == null) return null;
            return cb.equal(root.get("year"), year);
        };
    }

    public static Specification<Book> isbn(String isbn) {
        return (root, query, cb) -> {
            if (!isNotEmpty(isbn)) return null;
            return cb.equal(root.get("isbn"), isbn);
        };
    }

    public static Specification<Book> author(String firstName, String lastName) {
        return (root, query, cb) -> {
            if (!isNotEmpty(firstName) && !isNotEmpty(lastName)) return null;

            query.distinct(true);

            Join<Object, Object> author = root.join("author", JoinType.LEFT);
            Predicate predicate = cb.conjunction();

            if (isNotEmpty(firstName)) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(author.get("firstName")), "%" + firstName.toLowerCase() + "%"));
            }

            if (isNotEmpty(lastName)) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(author.get("lastName")), "%" + lastName.toLowerCase() + "%"));
            }

            return predicate;
        };
    }

    public static Specification<Book> genre(String genreName) {
        return (root, query, cb) -> {
            if (!isNotEmpty(genreName)) return null;

            query.distinct(true);

            Join<Object, Object> genres = root.join("genres", JoinType.LEFT);

            return cb.like(cb.lower(genres.get("label")), "%" + genreName.toLowerCase() + "%");
        };
    }

    public static Specification<Book> publisher(String publisherName) {
        return (root, query, cb) -> {
            if (!isNotEmpty(publisherName)) return null;

            Join<Object, Object> publisher = root.join("publisher", JoinType.LEFT);

            return cb.like(cb.lower(publisher.get("name")), "%" + publisherName.toLowerCase() + "%");
        };
    }

    public static Specification<Book> country(String countryName, String nationality) {
        return (root, query, cb) -> {
            if (!isNotEmpty(countryName) && !isNotEmpty(nationality)) return null;

            query.distinct(true);

            Join<Object, Object> author = root.join("author", JoinType.LEFT);
            Join<Object, Object> country = author.join("country", JoinType.LEFT);

            Predicate predicate = cb.conjunction();

            if (isNotEmpty(countryName)) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(country.get("name")), "%" + countryName.toLowerCase() + "%"));
            }

            if (isNotEmpty(nationality)) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(country.get("nationality")), "%" + nationality.toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}
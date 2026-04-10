package fr.bookhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.bookhub.entity.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findBookByIsbn(String isbn);
}

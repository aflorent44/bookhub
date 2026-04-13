package fr.bookhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.bookhub.entity.Author;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Optional<Author> findByLastName(String lastName);
}

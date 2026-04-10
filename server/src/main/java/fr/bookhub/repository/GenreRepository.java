package fr.bookhub.repository;

import fr.bookhub.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Optional<Genre> findByLabel(@Param("label") String label);
}

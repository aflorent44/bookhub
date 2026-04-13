package fr.bookhub.repository;

import fr.bookhub.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Optional<Publisher> findByName(@Param("name") String name);
}

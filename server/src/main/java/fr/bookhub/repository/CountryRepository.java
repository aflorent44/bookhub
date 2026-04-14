package fr.bookhub.repository;

import fr.bookhub.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
    Optional<Country> findByCode(String code);
    Optional<Country> findByNameIgnoreCase(String name);
}

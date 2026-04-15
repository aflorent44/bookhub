package fr.bookhub.repository;

import fr.bookhub.entity.Reservation;
import fr.bookhub.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUserId(Integer userId);
    List<Reservation> findByBookId(int bookId);
    List<Reservation> findByUserIdAndBookId(int userId, int bookId);
    List<Reservation> findByBookIdAndStatusOrderByCreatedAtAsc(int bookId, Status status);
}

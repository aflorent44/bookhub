package fr.bookhub.repository;

import fr.bookhub.dto.TopBookResponse;
import fr.bookhub.entity.Loan;
import fr.bookhub.entity.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    List<Loan> findByUserId(Integer userId);
    List<Loan> findByUserIdAndBookId(Integer userId, Integer bookId);
    List<Loan> findByBookId(Integer bookId);
    List<Loan> findByBookIdAndStatusOrderByCreatedAtAsc(int bookId, Status status);
    List<Loan> findByUserIdAndBookId(int userId, int bookId);
    List<Loan> findByStatus(Status status);
    @Query("""
        SELECT new fr.bookhub.dto.TopBookResponse(b.id, b.isbn, b.title, a.lastName, a.firstName, count(l))
        FROM Loan l
        JOIN Book b
        ON l.book.id = b.id
        JOIN Author a
        ON b.author.id = a.id
        WHERE l.status = 'FINISHED'
        GROUP BY b.id, b.isbn, b.title, a.lastName, a.firstName
        ORDER BY count(l) DESC
    """)
    List<TopBookResponse> findTopBooks(Pageable pageable);
}

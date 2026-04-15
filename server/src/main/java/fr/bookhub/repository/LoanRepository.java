package fr.bookhub.repository;

import fr.bookhub.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    List<Loan> findByUserId(Integer userId);
    List<Loan> findByUserIdAndBookId(Integer userId, Integer bookId);
    List<Loan> findByBookId(Integer bookId);
}

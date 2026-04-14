package fr.bookhub.service;

import fr.bookhub.entity.Book;
import fr.bookhub.entity.Loan;
import fr.bookhub.entity.Status;
import fr.bookhub.entity.User;
import fr.bookhub.repository.BookRepository;
import fr.bookhub.repository.LoanRepository;
import fr.bookhub.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LoanService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public ServiceResponse<?> createLoan(LoanCreateRequest req) {
        // Récupérer l'utilisateur "interne" (le bibliothéquaire) :
        Optional<User> internalUser = userRepository.findById(req.getInternalUserId());
        User foundInternalUser;

        if (internalUser.isPresent()) {
            foundInternalUser = internalUser.get();
        } else {
            return new ServiceResponse<>("7006", "Internal user not found");
        }

        // Vérifier si l'utilisateur existe :
        Optional<User> user = userRepository.findById(req.getUserId());
        User foundUser;

        if (user.isPresent()) {
            foundUser = user.get();
        } else {
            return new ServiceResponse<>("7005", "User not found");
        }

        // Vérifier la disponibilité du livre
        Optional<Book> book = bookRepository.findById(req.getBookId());
        Book foundBook;

        if (book.isPresent()) {
            foundBook = book.get();
            int availableQuantities = foundBook.getQuantity();

            // Le livre n'est pas disponible :
            if (availableQuantities <= 0) {
                return new ServiceResponse<>("7001", "Book not available");
            }
        } else {
            return new ServiceResponse<>("7002", "Book not found");
        }

        // Vérifier si l'utilisateur a des emprunts en retard
        List<Loan> userLoans = loanRepository.findByUserId(req.getUserId());

        // Cas où l'utilisateur a déjà fait des emprunts :
        if (!userLoans.isEmpty()) {
            int totalLoansInProgress = 0;

            for (Loan loan : userLoans) {
                // Cas où l'emprunt n'a pas été retournée
                LocalDateTime endDate = loan.getEndDate();

                // Si la date de fin de l'emprunt était avant aujourd'hui et que le statut de l'emprunt est en cours :
                if (endDate.isBefore(LocalDateTime.now()) && loan.getStatus() == Status.IN_PROGRESS) {
                    return new ServiceResponse<>("7003", "One or more books are late");
                }

                // Si la date de fin est après aujourd'hui et que le statut de l'emprunt est en cours :
                if (endDate.isAfter(LocalDateTime.now()) && loan.getStatus() == Status.IN_PROGRESS) {
                    totalLoansInProgress++;
                }
            }

            if (totalLoansInProgress >= 3) {
                return new ServiceResponse<>("7004", "Loan quota reached");
            }
        }

        // Créer l'objet Loan
        Loan newLoan = new Loan();
        newLoan.setDebutDate(LocalDateTime.now());
        newLoan.setEndDate(LocalDateTime.now().plusDays(14));
        newLoan.setReturnDate(null);
        newLoan.setStatus(Status.IN_PROGRESS);
        newLoan.setUser(foundUser);
        newLoan.setBook(foundBook);
        newLoan.setCreatedAt(LocalDateTime.now());
        newLoan.setUpdatedAt(LocalDateTime.now());
        newLoan.setUpdatedBy(foundInternalUser);

        // Décrémenter la quantité du livre et sauvegarder :
        foundBook.setQuantity(foundBook.getQuantity() - 1);
        foundBook.setUpdatedAt(LocalDateTime.now());
        foundBook.setUpdatedBy(foundInternalUser);
        bookRepository.save(foundBook);

        // Enregistrer l'emprunt
        Loan savedLoan = loanRepository.save(newLoan);

        // Renvoyer la réponse avec l'emprunt sauvegardé
        return new ServiceResponse<>("7000", "Loan successfully created",savedLoan);
    }
}

package fr.bookhub.service;

import fr.bookhub.dto.LoanCreateRequest;
import fr.bookhub.entity.Book;
import fr.bookhub.entity.Loan;
import fr.bookhub.entity.Status;
import fr.bookhub.entity.User;
import fr.bookhub.repository.BookRepository;
import fr.bookhub.repository.LoanRepository;
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
    private final UserService userService;
    private final LoanMapper loanMapper;

    public ServiceResponse<?> createLoan(LoanCreateRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("request is null");
        }
        if (req.getUserId() == null) {
            throw new IllegalArgumentException("userId is null");
        }
        if (req.getBookId() == null) {
            throw new IllegalArgumentException("bookId is null");
        }
        // Vérifier si l'utilisateur existe :
        ServiceResponse<User> responseUser = userService.getUserById(req.getUserId());

        if (responseUser.getCode().equals("8001")) {
            return responseUser;
        }

        User foundUser = responseUser.getData();

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
        newLoan.setStatus(Status.WAITING);
        newLoan.setUser(foundUser);
        newLoan.setBook(foundBook);
        newLoan.setCreatedAt(LocalDateTime.now());

        // Décrémenter la quantité du livre et sauvegarder :
        foundBook.setQuantity(foundBook.getQuantity() - 1);
        bookRepository.save(foundBook);

        // Enregistrer l'emprunt
        Loan savedLoan = loanRepository.save(newLoan);

        // Renvoyer la réponse avec l'emprunt sauvegardé
        return new ServiceResponse<>("7000", "Loan successfully created", loanMapper.toResponse(savedLoan));
    }

    public ServiceResponse<?> validate(LoanCreateRequest req) {
        // Récupérer le userInternal
        ServiceResponse<User> responseInternalUser = userService.getUserById(req.getInternalUserId());

        if (responseInternalUser.getCode().equals("8001")) {
            return responseInternalUser;
        }

        // Récupérer l'emprunt
        Optional<Loan> loan = loanRepository.findById(req.getLoanId());
        Loan foundLoan;

        if (loan.isPresent()) {
            foundLoan = loan.get();
        } else {
            return new ServiceResponse<>("7021", "Loan not found");
        }

        // Passer le statut en IN_PROGRESS
        foundLoan.setStatus(Status.IN_PROGRESS);
        foundLoan.setEndDate(LocalDateTime.now().plusDays(14));

        // Mettre à jour (updated_by, updated_at)
        foundLoan.setUpdatedAt(LocalDateTime.now());
        foundLoan.setUpdatedBy(responseInternalUser.getData());

        // Sauvegarder
        Loan savedLoan = loanRepository.save(foundLoan);

        return new ServiceResponse<>("7020", "Loan successfully validated", loanMapper.toResponse(savedLoan));
    }

    public ServiceResponse<?> finishOrCancelLoan(LoanCreateRequest req, Status status) {
        // Récupérer l'utilisateur "interne" (le bibliothéquaire) :
        ServiceResponse<User> responseInternalUser = userService.getUserById(req.getInternalUserId());

        if (responseInternalUser.getCode().equals("8001")) {
            return responseInternalUser;
        }

        User foundInternalUser = responseInternalUser.getData();

        // Vérifier si l'utilisateur existe :
        ServiceResponse<User> responseUser = userService.getUserById(req.getUserId());

        if (responseUser.getCode().equals("8001")) {
            return responseUser;
        }

        // Rechercher le livre dans les emprunts :
        Optional<Loan> loan = loanRepository.findById(req.getLoanId());
        Loan foundLoan;

        if (loan.isPresent()) {
            foundLoan = loan.get();
        } else {
            return new ServiceResponse<>("7011", "Loan not found");
        }

        // Mettre à jour le livre
        Optional<Book> book = bookRepository.findById(req.getBookId());
        Book foundBook;

        if (book.isPresent()) {
            foundBook = book.get();
        }  else {
            return new ServiceResponse<>("7012", "Book not found");
        }

        if (status == Status.FINISHED) {
            if (foundLoan.getStatus() != Status.IN_PROGRESS) {
                return new ServiceResponse<>("7013", "User can't return the loan because the status is not in progress");
            }
            // Mettre à jour l'emprunt
            foundLoan.setStatus(Status.FINISHED);
            foundLoan.setReturnDate(LocalDateTime.now());
        }

        if (status == Status.CANCELED) {
            if (foundLoan.getStatus() != Status.WAITING) {
                return new ServiceResponse<>("7014", "User can't cancel the loan because the status is not waiting");
            }
            foundLoan.setStatus(Status.CANCELED);
            foundLoan.setReturnDate(null);
        }

        foundLoan.setUpdatedAt(LocalDateTime.now());
        foundLoan.setUpdatedBy(foundInternalUser);
        foundBook.setQuantity(foundBook.getQuantity() + 1);
        foundBook.setUpdatedAt(LocalDateTime.now());
        foundBook.setUpdatedBy(foundInternalUser);

        // Sauvegarder l'emprunt :
        Loan savedLoan = loanRepository.save(foundLoan);

        // Sauvegarder le livre :
        bookRepository.save(foundBook);

        if (status == Status.FINISHED) {
            return new ServiceResponse<>("7010", "Book successfully returned", loanMapper.toResponse(savedLoan));
        }
        return new ServiceResponse<>("7015", "Loan successfully canceled",  loanMapper.toResponse(savedLoan));
    }

    public ServiceResponse<List<?>> getLoansByBookId(int bookId) {
        List<Loan> loans = loanRepository.findByBookId(bookId);
        return new ServiceResponse<>("7000", "Loans successfully retrieved", loanMapper.toResponse(loans));
    }

    public ServiceResponse<List<?>> getLoansByUserIdAndBookId(int userId, int bookId) {
        List<Loan> loans = loanRepository.findByUserIdAndBookId(userId, bookId);
        if (loans.isEmpty()) {
            return new ServiceResponse<>("7040", "No loans found");
        }
        return new ServiceResponse<>("7041", "Loans found", loanMapper.toResponse(loans));
    }
}

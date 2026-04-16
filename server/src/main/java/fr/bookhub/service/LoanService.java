package fr.bookhub.service;

import fr.bookhub.dto.LoanCreateRequest;
import fr.bookhub.dto.LoanMapper;
import fr.bookhub.dto.LoanResponse;
import fr.bookhub.entity.*;
import fr.bookhub.repository.BookRepository;
import fr.bookhub.repository.LoanRepository;
import fr.bookhub.repository.ReservationRepository;
import fr.bookhub.utility.ApiCode;
import fr.bookhub.utility.ApiException;
import fr.bookhub.utility.ServiceResponse;
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
    private final ReservationRepository reservationRepository;
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
                throw new ApiException(ApiCode.LOAN_NOT_FOUND);
            }
        } else {
            throw new ApiException(ApiCode.LOAN_BOOK_NOT_FOUND);
        }

        // Vérifier si l'utilisateur a des emprunts en retard
        List<Loan> userLoans = loanRepository.findByUserId(req.getUserId());

        // Cas où l'utilisateur a déjà fait des emprunts :
        if (!userLoans.isEmpty()) {
            int totalLoansInProgress = 0;

            // check sur les emprunts en cours et en attente de récupération
            for (Loan loan : userLoans) {
                // Cas où l'emprunt n'a pas été retournée
                LocalDateTime endDate = loan.getEndDate();

                // Si la date de fin de l'emprunt était avant aujourd'hui et que le statut de l'emprunt est en cours :
                if (endDate.isBefore(LocalDateTime.now()) && loan.getStatus() == Status.IN_PROGRESS) {
                    throw new ApiException(ApiCode.LOAN_LATE_EXISTS);
                }
                if (loan.getStatus() == Status.IN_PROGRESS || loan.getStatus() == Status.WAITING) {
                    totalLoansInProgress++;
                }
            }

            if (totalLoansInProgress >= 3) {
                throw new ApiException(ApiCode.LOAN_QUOTA_REACHED);
            }
        }

        List<Loan> existingLoans = loanRepository.findByUserIdAndBookId(req.getUserId(), req.getBookId());

        boolean hasActiveOrPendingLoan = existingLoans.stream()
                .anyMatch(l -> l.getStatus() == Status.WAITING || l.getStatus() == Status.IN_PROGRESS);

        if (hasActiveOrPendingLoan) {
            throw new ApiException(ApiCode.LOAN_ALREADY_EXISTS_FOR_BOOK);
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
        return new ServiceResponse<>(ApiCode.LOAN_CREATED, loanMapper.toResponse(savedLoan));
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
            throw new ApiException(ApiCode.LOAN_VALIDATE_NOT_FOUND);
        }

        // Passer le statut en IN_PROGRESS
        foundLoan.setStatus(Status.IN_PROGRESS);
        foundLoan.setEndDate(LocalDateTime.now().plusDays(14));

        // Mettre à jour (updated_by, updated_at)
        foundLoan.setUpdatedAt(LocalDateTime.now());
        foundLoan.setUpdatedBy(responseInternalUser.getData());

        // Sauvegarder
        Loan savedLoan = loanRepository.save(foundLoan);

        return new ServiceResponse<>(ApiCode.LOAN_VALIDATED, loanMapper.toResponse(savedLoan));
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
            throw new ApiException(ApiCode.LOAN_NOT_FOUND);
        }

        // Mettre à jour le livre
        Optional<Book> book = bookRepository.findById(req.getBookId());
        Book foundBook;

        if (book.isPresent()) {
            foundBook = book.get();
        }  else {
            throw new ApiException(ApiCode.LOAN_RETURN_BOOK_NOT_FOUND);
        }

        if (status == Status.FINISHED) {
            if (foundLoan.getStatus() != Status.IN_PROGRESS) {
                throw new ApiException(ApiCode.LOAN_INVALID_RETURN_STATUS);
            }
            // Mettre à jour l'emprunt
            foundLoan.setStatus(Status.FINISHED);
            foundLoan.setReturnDate(LocalDateTime.now());
        }

        if (status == Status.CANCELED) {
            if (foundLoan.getStatus() != Status.WAITING) {
                throw new ApiException(ApiCode.LOAN_INVALID_CANCEL_STATUS);
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

        handleWaitingList(foundBook, foundInternalUser);

        if (status == Status.FINISHED) {
            return new ServiceResponse<>(ApiCode.LOAN_RETURNED, loanMapper.toResponse(savedLoan));
        }
        return new ServiceResponse<>(ApiCode.LOAN_CANCELED,  loanMapper.toResponse(savedLoan));
    }

    public ServiceResponse<List<?>> getLoansByBookId(int bookId) {
        List<Loan> loans = loanRepository.findByBookId(bookId);
        return new ServiceResponse<>(ApiCode.LOAN_CREATED, loanMapper.toResponse(loans));
    }

    public ServiceResponse<List<?>> getLoansByUserIdAndBookId(int userId, int bookId) {
        List<Loan> loans = loanRepository.findByUserIdAndBookId(userId, bookId);
        if (loans.isEmpty()) {
            throw new ApiException(ApiCode.LOAN_NOT_FOUND);
        }
        return new ServiceResponse<>(ApiCode.LOAN_VALIDATED, loanMapper.toResponse(loans));
    }

    private void handleWaitingList(Book book, User internalUser) {
        // 1. Chercher l'emprunt en attente le plus ancien pour ce livre
        List<Loan> waitingLoans = loanRepository.findByBookIdAndStatusOrderByCreatedAtAsc(
                book.getId(), Status.WAITING
        );

        if (!waitingLoans.isEmpty()) {
            Loan oldestWaitingLoan = waitingLoans.getFirst();
            oldestWaitingLoan.setStatus(Status.IN_PROGRESS);
            oldestWaitingLoan.setDebutDate(LocalDateTime.now());
            oldestWaitingLoan.setEndDate(LocalDateTime.now().plusDays(14));
            oldestWaitingLoan.setUpdatedAt(LocalDateTime.now());
            oldestWaitingLoan.setUpdatedBy(internalUser);
            loanRepository.save(oldestWaitingLoan);

            bookRepository.save(book);
            return; // Un emprunt en attente a été activé, pas besoin de vérifier les réservations
        }

        // 2. Si pas d'emprunt en attente et quantité > 0, convertir la réservation la plus ancienne
        if (book.getQuantity() > 0) {
            List<Reservation> waitingReservations = reservationRepository
                    .findByBookIdAndStatusOrderByCreatedAtAsc(book.getId(), Status.WAITING);

            if (!waitingReservations.isEmpty()) {
                Reservation oldestReservation = waitingReservations.getFirst();

                // Créer un emprunt depuis la réservation
                Loan newLoan = new Loan();
                newLoan.setUser(oldestReservation.getUser());
                newLoan.setBook(book);
                newLoan.setStatus(Status.WAITING); // En attente que l'user vienne chercher le livre
                newLoan.setDebutDate(LocalDateTime.now());
                newLoan.setEndDate(LocalDateTime.now().plusDays(14));
                newLoan.setCreatedAt(LocalDateTime.now());
                newLoan.setUpdatedAt(LocalDateTime.now());
                newLoan.setUpdatedBy(internalUser);
                loanRepository.save(newLoan);

                // Supprimer la réservation
                reservationRepository.deleteById(oldestReservation.getId());

                // Décrémenter la quantité
                book.setQuantity(book.getQuantity() - 1);
                bookRepository.save(book);
            }
        }
    }


    public ServiceResponse<List<LoanResponse>> getMyLoansWithHistory(String email) {
        User user = userService.findByEmail(email);
        List<Loan> loans = loanRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<LoanResponse> loanResponseList = loans.stream()
                .map(loanMapper::toResponse)
                .toList();

        return new ServiceResponse<>("7050", "Loans successfully retrieved", loanResponseList);
    }
}

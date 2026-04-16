package fr.bookhub.service;

import fr.bookhub.dto.dashboard.DashboardResponse;
import fr.bookhub.dto.dashboard.RoleCreateRequest;
import fr.bookhub.dto.dashboard.TopBookResponse;
import fr.bookhub.dto.loan.LoanBasicMapper;
import fr.bookhub.dto.loan.LoanBasicResponse;
import fr.bookhub.dto.user.UserBasicMapper;
import fr.bookhub.dto.user.UserBasicResponse;
import fr.bookhub.entity.Loan;
import fr.bookhub.entity.Role;
import fr.bookhub.entity.Status;
import fr.bookhub.entity.User;
import fr.bookhub.repository.BookRepository;
import fr.bookhub.repository.LoanRepository;
import fr.bookhub.repository.UserRepository;
import fr.bookhub.utility.ApiCode;
import fr.bookhub.utility.ApiException;
import fr.bookhub.utility.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

@Service
@AllArgsConstructor
public class DashboardService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final LoanBasicMapper loanBasicMapper;
    private final UserRepository userRepository;
    private final UserBasicMapper userBasicMapper;
    private final UserService userService;

    // Nombre de livre total
    private long getTotalBooks() {
        return bookRepository.count();
    }

    // Nombre d'emprunts actif
    private List<LoanBasicResponse> getActiveLoans() {
        return loanRepository.findByStatus(Status.IN_PROGRESS)
                .stream()
                .map(loanBasicMapper::toResponse)
                .toList();
    }

    // Liste des retards
    private List<LoanBasicResponse> getLateLoans() {
        List<Loan> loans = loanRepository.findAll();

        System.out.println(loans.stream().map(Loan::getEndDate).toString());
        LocalDateTime now = LocalDateTime.now();

        loans.forEach(l ->
                System.out.println(l.getId() + " - " + l.getStatus() + " - " + l.getEndDate())
        );

        System.out.println("NOW = " + LocalDateTime.now());

        return loans.stream()
                .filter((loan) -> loan.getEndDate().isBefore(now) && loan.getStatus() == Status.IN_PROGRESS)
                .map(loanBasicMapper::toResponse)
                .toList();

    }

    // Livres les plus empruntés (top 10)
    private List<TopBookResponse> getTopLoans() {
        Pageable pageable = PageRequest.of(0, 10);
        return loanRepository.findTopBooks(pageable);
    }

    public DashboardResponse getLibrarianDashboard() {
        return new DashboardResponse(getTotalBooks(), getActiveLoans(), getLateLoans(), getTopLoans());
    }

    public List<UserBasicResponse> getAdminDashboard() {
        // Liste des utilisateurs
        List<User> users = userRepository.findAll();
        return users.stream().map(userBasicMapper::toResponse).toList();
    }

    public ServiceResponse<?> updateUserRole(RoleCreateRequest req) {
        // Vérifier si l'utilisateur existe
        ServiceResponse<User> userResponse = userService.getUserById(req.userId());

        if (userResponse.getCode().equals("8001")) {
            return userResponse;
        }

        // Vérifier si l'internal user existe
        ServiceResponse<User> internalUserResponse = userService.getUserById(req.userId());

        if (internalUserResponse.getCode().equals("8001")) {
            return internalUserResponse;
        }

        // Vérifier que l'internal user a le droit de changer le rôle (doit être admin) :
        Role internalUserRole = internalUserResponse.getData().getRole();

        if (internalUserRole != Role.ADMIN) {
            throw new ApiException(ApiCode.ROLE_FORBIDDEN);
        }

        // Vérifier si le rôle est cohérent
        if (!EnumSet.of(Role.USER, Role.LIBRARIAN, Role.ADMIN).contains(req.role())) {
            throw new ApiException(ApiCode.ROLE_INVALID);
        }

        // Entité :
        User user = userResponse.getData();
        user.setRole(req.role());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(internalUserResponse.getData());

        // Sauvegarder
        userRepository.save(user);

        // Renvoyer la réponse avec l'update
        return new ServiceResponse<>(ApiCode.ROLE_UPDATED, userBasicMapper.toResponse(user));
    }
}

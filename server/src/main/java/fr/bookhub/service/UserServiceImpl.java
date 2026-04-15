package fr.bookhub.service;

import fr.bookhub.dto.UserRegistrationRequest;
import fr.bookhub.entity.Loan;
import fr.bookhub.entity.Role;
import fr.bookhub.entity.User;
import fr.bookhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(UserRegistrationRequest requestUser) {
        if (userRepository.existsByEmail(requestUser.email())) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }

        if (userRepository.existsByPseudo(requestUser.username())) {
            throw new RuntimeException("Ce pseudo est déjà utilisé.");
        }

        User user = new User();
        user.setLastName(requestUser.lastname().trim());
        user.setFirstName(requestUser.firstname().trim());
        user.setPseudo(requestUser.username().trim());
        user.setEmail(requestUser.email().trim().toLowerCase());
        user.setUserPassword(passwordEncoder.encode(requestUser.password()));

        user.setRole(Role.USER);
        user.setShowRealName(false);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByPseudo(username);
    }

    public ServiceResponse<User> getUserById(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        return user.map(value ->
                new ServiceResponse<>("8000", "User found", value))
                    .orElseGet(() ->
                            new ServiceResponse<>("8001", "User not found"));
    }

}

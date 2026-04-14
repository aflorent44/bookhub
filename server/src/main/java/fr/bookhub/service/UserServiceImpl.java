package fr.bookhub.service;

import fr.bookhub.dto.UpdateProfileRequest;
import fr.bookhub.dto.UserRegistrationRequest;
import fr.bookhub.dto.UserResponse;
import fr.bookhub.entity.Role;
import fr.bookhub.entity.User;
import fr.bookhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
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

    @Override
    @Transactional
    public UserResponse getProfile(String email) {
        User user = findByEmail(email);
        return new UserResponse(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPseudo(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }

    @Override
    public void deleteAccount(String email) {
        User user = findByEmail(email);
        userRepository.delete(user);
    }

    @Override
    public void updateProfile(String email, UpdateProfileRequest request) {
        User user = findByEmail(email);

        if (request.firstName() != null) {
            user.setFirstName(request.firstName().trim());
        }

        if (request.lastName() != null) {
            user.setLastName(request.lastName().trim());
        }

        if (request.phoneNumber() != null) {
            user.setPhoneNumber(request.phoneNumber().trim());
        }

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}

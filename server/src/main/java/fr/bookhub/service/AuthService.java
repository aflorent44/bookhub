package fr.bookhub.service;

import fr.bookhub.dto.auth.AuthResponse;
import fr.bookhub.dto.auth.LoginRequest;
import fr.bookhub.dto.user.UserResponse; // N'oublie pas l'import
import fr.bookhub.entity.User;
import fr.bookhub.repository.UserRepository;
import fr.bookhub.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

        String token = jwtService.createToken(user);

        UserResponse userDto = new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPseudo(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole() != null ? user.getRole().name() : "USER",
                user.getCreatedAt()
        );

        return new AuthResponse(
                token,
                "Bearer",
                userDto
        );
    }
}
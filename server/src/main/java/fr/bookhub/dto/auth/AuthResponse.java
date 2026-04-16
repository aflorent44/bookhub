package fr.bookhub.dto.auth;

import fr.bookhub.dto.user.UserResponse;

public record AuthResponse(
        String token,
        String tokenType,
        UserResponse user
) {
}

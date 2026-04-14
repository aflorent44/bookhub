package fr.bookhub.dto;

public record AuthResponse(
        String token,
        String tokenType,
        UserResponse user
) {
}

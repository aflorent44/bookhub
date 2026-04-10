package fr.bookhub.dto;

public record AuthResponse(
        String token,

        String tokenType,

        Integer userId,

        String email,

        String pseudo,

        String role
) {
}

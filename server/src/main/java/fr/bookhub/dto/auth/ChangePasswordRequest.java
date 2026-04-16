package fr.bookhub.dto.auth;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}

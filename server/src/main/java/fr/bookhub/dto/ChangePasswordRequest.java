package fr.bookhub.dto;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}

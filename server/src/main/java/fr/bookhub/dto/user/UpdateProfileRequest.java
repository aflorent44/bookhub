package fr.bookhub.dto.user;

public record UpdateProfileRequest(
        String firstName,
        String lastName,
        String phoneNumber
) {
}

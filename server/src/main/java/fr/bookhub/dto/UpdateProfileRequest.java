package fr.bookhub.dto;

public record UpdateProfileRequest(
        String firstName,
        String lastName,
        String phoneNumber
) {
}

package fr.bookhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(

        @NotBlank(message = "Le nom est obligatoire.")
        @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères.")
        String lastname,

        @NotBlank(message = "Le prénom est obligatoire.")
        @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères.")
        String firstname,

        @NotBlank
        @Size(min = 3, max = 30, message = "Le pseudo doit contenir entre 3 et 30 caractères.")
        String username,

        @NotBlank(message = "L'email est obligatoire.")
        @Email(message = "Format d'email invalide.")
        String email,

        @Size(min = 12, message = "Le mot de passe doit contenir au moins 12 caractères.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$", message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial.")
        String password
) {
}
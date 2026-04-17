package fr.bookhub.dto;

import fr.bookhub.entity.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoleCreateRequest(
        @Positive
        Integer userId,
        @Positive
        Integer internalUserId,
        @NotNull
        Role role
) {
}

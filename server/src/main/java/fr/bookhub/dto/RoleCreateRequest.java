package fr.bookhub.dto;

import fr.bookhub.entity.Role;

public record RoleCreateRequest(
        Integer userId,
        Integer internalUserId,
        Role role
) {
}

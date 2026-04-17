package fr.bookhub.dto.dashboard;

import fr.bookhub.entity.Role;

public record RoleCreateRequest(
        Integer userId,
        Integer internalUserId,
        Role role
) {
}

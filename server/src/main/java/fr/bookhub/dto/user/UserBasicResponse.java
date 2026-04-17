package fr.bookhub.dto.user;

import fr.bookhub.entity.Role;

public record UserBasicResponse (
        Integer id,
        String lastName,
        String firstName,
        String pseudo,
        String email,
        String phoneNumber,
        Role role
) {

}

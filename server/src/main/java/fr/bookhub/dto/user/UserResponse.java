package fr.bookhub.dto.user;

import java.time.LocalDateTime;

public record UserResponse(

        Integer id,

        String firstname,

        String lastname,

        String username,

        String email,

        String phoneNumber,

        String role,

        LocalDateTime createdAt
) {
}

package fr.bookhub.service;

import fr.bookhub.dto.UserRegistrationRequest;
import fr.bookhub.entity.User;

public interface UserService {

    User registerUser(UserRegistrationRequest requestUser);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}

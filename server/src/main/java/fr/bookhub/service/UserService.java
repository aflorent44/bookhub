package fr.bookhub.service;

import fr.bookhub.dto.UserRegistrationRequest;
import fr.bookhub.dto.UserResponse;
import fr.bookhub.entity.User;

public interface UserService {

    User registerUser(UserRegistrationRequest requestUser);

    User findByEmail(String email);

    boolean existsByUsername(String username);

    ServiceResponse<User> getUserById(Integer userId);

    UserResponse getProfile(String email);

    void deleteAccount(String email);
}

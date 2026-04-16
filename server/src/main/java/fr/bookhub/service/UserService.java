package fr.bookhub.service;

import fr.bookhub.dto.ChangePasswordRequest;
import fr.bookhub.dto.UpdateProfileRequest;
import fr.bookhub.dto.UserRegistrationRequest;
import fr.bookhub.dto.UserResponse;
import fr.bookhub.entity.User;
import fr.bookhub.utility.ServiceResponse;

public interface UserService {

    User registerUser(UserRegistrationRequest requestUser);

    User findByEmail(String email);

    boolean existsByUsername(String username);

    ServiceResponse<User> getUserById(Integer userId);

    UserResponse getProfile(String email);

    void deleteAccount(String email);

    void updateProfile(String email, UpdateProfileRequest request);

    void changePassword(String email, ChangePasswordRequest request);
}

package fr.bookhub.dto.user;

import fr.bookhub.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserBasicMapper {

    public UserBasicResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserBasicResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPseudo(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }
}

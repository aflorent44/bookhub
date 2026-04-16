package fr.bookhub.dto;

import fr.bookhub.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserBasicMapper {

    public UserBasicResponse toResponse(User user) {
        UserBasicResponse dto = new UserBasicResponse();

        if (user == null) {
            return null;
        }

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPseudo(user.getPseudo());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());

        return dto;
    }
}

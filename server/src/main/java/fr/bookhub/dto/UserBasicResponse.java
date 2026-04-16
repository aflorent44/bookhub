package fr.bookhub.dto;

import fr.bookhub.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicResponse {
    private Integer id;
    private String lastName;
    private String firstName;
    private String pseudo;
    private String email;
    private String phoneNumber;
    private Role role;
}

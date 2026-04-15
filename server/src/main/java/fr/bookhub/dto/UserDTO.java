package fr.bookhub.dto;

import fr.bookhub.entity.Role;
import fr.bookhub.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String lastName;
    private String firstName;
    private String pseudo;
    private Boolean showRealName;
    private String phoneNumber;
    private String email;
    private String userPassword;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User createdBy;
    private User updatedBy;
}

package fr.bookhub.controller;

import fr.bookhub.entity.User;
import fr.bookhub.service.ServiceResponse;
import fr.bookhub.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ServiceResponse<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

}
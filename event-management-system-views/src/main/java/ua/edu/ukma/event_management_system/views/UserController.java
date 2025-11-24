package ua.edu.ukma.event_management_system.views;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.event_management_system.clients.UserClient;
import ua.edu.ukma.event_management_system.dto.UserDto;
import ua.edu.ukma.event_management_system.dto.UserRole;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("user")
public class UserController {

    private UserClient userClient;

    @Autowired
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/")
    public String getUsers(Model model) {
        List<UserDto> users;
        users = userClient.getAllUsers();
        model.addAttribute("users", users);
        return "users/user-list";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable long id, Model model) {
        UserDto user = userClient.getUserById(id).getBody();
        model.addAttribute("user", user);
        return "users/user-details";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable long id, Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDto currentUser = userClient.getUser(userDetails.getUsername()).getBody();

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (currentUser.getUserRole() != UserRole.ADMIN && currentUser.getId() != id) {
            model.addAttribute("errors", List.of("You cannot modify user that is not you!"));
            return "error";
        }
        UserDto user = userClient.getUserById(id).getBody();

        if (user == null) {
            return "redirect:/login";
        }

        String formattedDate = user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("dateOfBirthString", formattedDate);
        model.addAttribute("userDto", user);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("canShow", currentUser.getUserRole() == UserRole.ADMIN);
        return "users/user-form";
    }

    @PutMapping("/{id}")
    public String updateBuilding(@PathVariable long id, @Valid UserDto userDto,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            List<String> errors = new ArrayList<>();
            bindingResult.getFieldErrors().forEach(error -> {
                String errorMessage = "Error for " + error.getField() + " field: " + error.getDefaultMessage();
                errors.add(errorMessage);
            });
            model.addAttribute("errors", errors);
            return "error";
        }
        userClient.updateUser(id, userDto);
        return "redirect:/user/" + id;
    }

}
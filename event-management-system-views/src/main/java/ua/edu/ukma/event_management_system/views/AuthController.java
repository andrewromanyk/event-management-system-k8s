package ua.edu.ukma.event_management_system.views;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.event_management_system.clients.UserClient;
import ua.edu.ukma.event_management_system.dto.UserDto;
import ua.edu.ukma.event_management_system.dto.UserRole;

@Controller
public class AuthController {

    private UserClient userClient;

    @Autowired
    public void setUserService(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("register")
    public String index(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        ResponseEntity<String> response = userClient.getToken(username, password);
        String token = response.getBody();

        ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * (long) 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.LOCATION, "/myprofile")
                .body("Login successful");
    }


    @PostMapping("register")
    public String registerUser(@ModelAttribute @Valid UserDto userDto) {
        userDto.setUserRole(UserRole.USER);
        userClient.createUser(userDto);
        return "redirect:/login";
    }
}

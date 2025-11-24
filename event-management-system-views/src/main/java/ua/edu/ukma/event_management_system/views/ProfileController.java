package ua.edu.ukma.event_management_system.views;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.edu.ukma.event_management_system.clients.UserClient;
import ua.edu.ukma.event_management_system.dto.UserDto;

@Controller
public class ProfileController {

    private UserClient userClient;

    @Autowired
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/myprofile")
    public String profile(Model model) {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDto user = userClient.getUser(details.getUsername()).getBody();
        model.addAttribute("user", user);
        return "profile/myprofile";
    }

    @GetMapping("/tologout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "main";
    }

}

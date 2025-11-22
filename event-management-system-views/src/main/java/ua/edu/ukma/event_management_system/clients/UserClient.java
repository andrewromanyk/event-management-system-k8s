package ua.edu.ukma.event_management_system.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ukma.event_management_system.dto.UserDto;

import java.util.List;

@FeignClient(name = "user-client", url = "${feign.client.user.url}")
public interface UserClient {

    @GetMapping("/api/user/{username}")
    ResponseEntity<UserDto> getUser(@PathVariable String username);

    //by id
    @GetMapping("/api/user/json/{id}")
    ResponseEntity<UserDto> getUserById(@PathVariable Long id);

    @PostMapping("login")
    ResponseEntity<String> getToken(@RequestParam String username, @RequestParam String password);

    @PostMapping("/api/user")
    ResponseEntity<?> createUser(UserDto userDto);

    //get all users
    @GetMapping("/api/user")
    List<UserDto> getAllUsers();

    @PutMapping("/api/user/{id}")
    ResponseEntity<?> updateUser(@RequestParam Long id, @RequestBody UserDto userDto);
}

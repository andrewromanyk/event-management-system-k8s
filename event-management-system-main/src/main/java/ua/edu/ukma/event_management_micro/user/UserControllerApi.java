package ua.edu.ukma.event_management_micro.user;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserControllerApi {

    private UserService userService;
    @Autowired
    public void setBuildingService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {
        try {
            UserDto userDto = userService.getUserById(id);
            return ResponseEntity.ok(userDto);
        }
        catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createNewUser(@RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        userService.createUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UserDto userDto, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
                );
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }
            userService.updateUser(id, userDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }finally{
            MDC.clear();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        try {
            boolean userRemoved = userService.removeUser(id);
            if(!userRemoved) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }finally {
            MDC.clear();
        }
    }

}

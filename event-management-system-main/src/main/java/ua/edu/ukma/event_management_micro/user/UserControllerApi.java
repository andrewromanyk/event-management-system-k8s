package ua.edu.ukma.event_management_micro.user;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("api/user")
public class UserControllerApi {

    private UserService userService;
    @Autowired
    public void setBuildingService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "json/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUserJson(@PathVariable long id) {
        try {
            UserDto userDto = userService.getUserById(id);
            return ResponseEntity.ok(userDto);
        }
        catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getUserHtml(@PathVariable long id) {
        UserDto userDto = userService.getUserById(id);
        String html = createUserHtmlPage(userDto);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    //get user by username
    @GetMapping(value = "{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        try {
            UserDto userDto = userService.getUserByUsername(username);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    private String createUserHtmlPage(UserDto userDto) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <title>" + userDto.getUsername() + "</title>\n" +
                "  <style>\n" +
                "    .container { display: flex; align-items: center; justify-content: center; flex-direction: column; text-align: center; }\n" +
                "    .form_area { display: flex; justify-content: center; align-items: center; flex-direction: column; background-color: #EDDCD9; height: auto; width: auto; border: 2px solid #264143; border-radius: 20px; box-shadow: 3px 4px 0px 1px #E99F4C; }\n" +
                "    .title { color: #264143; font-weight: 900; font-size: 1.5em; margin-top: 20px; }\n" +
                "    .sub_title { font-weight: 600; margin: 5px 0; }\n" +
                "    .form_group { display: flex; flex-direction: column; align-items: baseline; margin: 10px; }\n" +
                "    .form_style { outline: none; border: 2px solid #264143; box-shadow: 3px 4px 0px 1px #E99F4C; width: 290px; padding: 12px 10px; border-radius: 4px; font-size: 15px; }\n" +
                "    .form_style:focus { transform: translateY(4px); box-shadow: 1px 2px 0px 0px #E99F4C; }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class='container'>\n" +
                "    <div class='form_area'>\n" +
                "      <p class='title'>" + userDto.getUsername() + "</p>\n" +
                "      <div class='form_group'><label class='sub_title'>ID</label><input value='" + userDto.getId() + "' class='form_style' type='text' readonly></div>\n" +
                "      <div class='form_group'><label class='sub_title'>Role</label><input value='" + userDto.getUserRole() + "' class='form_style' type='text' readonly></div>\n" +
                "      <div class='form_group'><label class='sub_title'>First Name</label><input value='" + userDto.getFirstName() + "' class='form_style' type='text' readonly></div>\n" +
                "      <div class='form_group'><label class='sub_title'>Last Name</label><input value='" + userDto.getLastName() + "' class='form_style' type='text' readonly></div>\n" +
                "      <div class='form_group'><label class='sub_title'>Email</label><input value='" + userDto.getEmail() + "' class='form_style' type='email' readonly></div>\n" +
                "      <div class='form_group'><label class='sub_title'>Phone</label><input value='" + userDto.getPhoneNumber() + "' class='form_style' type='text' readonly></div>\n" +
                "      <div class='form_group'><label class='sub_title'>Date of Birth</label><input value='" + userDto.getDateOfBirth() + "' class='form_style' type='text' readonly></div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createNewUser(@RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        UserDto created = userService.createUser(userDto);
        URI location = URI.create("api/user/" + created.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
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

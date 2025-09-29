package ua.edu.ukma.event_management_micro.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class UserDto {
    private Long id;
    private UserRole userRole;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    public UserDto(Long id, UserRole userRole, String username, String firstName, String lastName, String email, String phoneNumber, LocalDate dateOfBirth) {
        this.id = id;
        this.userRole = userRole;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public UserDto(Long id, UserRole userRole, String username, String firstName, String lastName, String email, String password, String phoneNumber, LocalDate dateOfBirth) {
        this.id = id;
        this.userRole = userRole;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }
}

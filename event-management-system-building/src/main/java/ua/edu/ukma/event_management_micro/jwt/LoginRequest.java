package ua.edu.ukma.event_management_micro.jwt;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

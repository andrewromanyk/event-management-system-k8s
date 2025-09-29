package ua.edu.ukma.event_management_micro.user;

import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ua.edu.ukma.event_management_micro.core.LogEvent;
import ua.edu.ukma.event_management_micro.user.jwt.JwtService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Component
public class UserService {

    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    void setModelMapper(@Lazy ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Autowired
    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setAuthenticationManager(@Lazy AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public UserDto createUser(UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserDto returned = toDomain(userRepository.save(dtoToEntity(user)));
        applicationEventPublisher.publishEvent(new LogEvent(this, "New user created: " + returned.getUsername()));
        return returned;
    }

    public List<UserDto> getAllUsers() {
        applicationEventPublisher.publishEvent(new LogEvent(this, "Get all users called"));
        return userRepository.findAll().
                stream()
                .map(this::toDomain)
                .toList();
    }

    public UserDto getUserById(long userId) {
        return toDomain(userRepository.findById(userId).orElseThrow());
    }

    public UserDto getUserByUsername(String username) {
        return toDomain(userRepository.findByUsername(username));
    }

    public void updateUser(long id, UserDto updatedUser) {
        Optional<UserEntity> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            UserEntity existingUser = existingUserOpt.get();
            if (updatedUser.getUserRole()!=null) existingUser.setUserRole(updatedUser.getUserRole());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setDateOfBirth(updatedUser.getDateOfBirth());

            userRepository.save(existingUser);
        }
    }

    public boolean removeUser(long userId) {
        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public boolean checkPermission(UserDto user, UserRole role) {
        List<UserRole> list = Arrays.asList(UserRole.values());
        return list.indexOf(user.getUserRole()) >= list.indexOf(role);
    }

    public String verify(String username, String password) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (auth.isAuthenticated()) {
            return jwtService.generateToken(username);
        } else {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401));
        }
    }

    private UserDto toDomain(UserEntity user) {
        return modelMapper.map(user, UserDto.class);
    }

    private UserEntity dtoToEntity(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}

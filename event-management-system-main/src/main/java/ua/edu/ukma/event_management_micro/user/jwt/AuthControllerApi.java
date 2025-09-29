package ua.edu.ukma.event_management_micro.user.jwt;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.edu.ukma.event_management_micro.user.UserDto;
import ua.edu.ukma.event_management_micro.user.UserRole;
import ua.edu.ukma.event_management_micro.user.UserService;

@RestController
public class AuthControllerApi {

	private UserService userService;
	private JwtService jwtService;

	@Autowired
	public void setJwtService(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("login")
	public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
		if (username == null || password == null) {
			throw new ResponseStatusException(HttpStatusCode.valueOf(401));
		}

		String token = userService.verify(username, password);

		return ResponseEntity
				.status(HttpStatus.FOUND)
				.body("Token: " + token);
	}

	@GetMapping("/tologout")
	public ResponseEntity<String> logout() {
		ResponseCookie cookie = ResponseCookie.from("jwtToken")
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(0)
				.sameSite("Strict")
				.build();

		return ResponseEntity
				.status(HttpStatus.FOUND)
				.header(HttpHeaders.SET_COOKIE, cookie.toString())
				.header(HttpHeaders.LOCATION, "/FAQ.html")
				.body("Logout successful");
	}

	@PostMapping("register")
	public String registerUser(@RequestBody UserDto userDto) {
		userDto.setUserRole(UserRole.USER);
		userService.createUser(userDto);
		return "Successfully registered!";
	}

}

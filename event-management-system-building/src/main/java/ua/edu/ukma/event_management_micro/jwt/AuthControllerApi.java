package ua.edu.ukma.event_management_micro.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthControllerApi {

	private JwtService jwtService;

	@Value("${secret.name}")
	public String secretName;
	@Value("${secret.pass}")
	public String secretPass;

	@Autowired
	public void setJwtService(JwtService jwtService) {
		this.jwtService = jwtService;
	}


	@PostMapping("api-login")
	public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
		if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
			return new ResponseEntity<>("Missing parameter", HttpStatus.BAD_REQUEST);
		}

		if (!loginRequest.getUsername().equals(secretName) || !loginRequest.getPassword().equals(secretPass)) {
			return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
		}

		return ResponseEntity
				.status(HttpStatus.ACCEPTED)
				.body(jwtService.generateToken("api"));
	}

}

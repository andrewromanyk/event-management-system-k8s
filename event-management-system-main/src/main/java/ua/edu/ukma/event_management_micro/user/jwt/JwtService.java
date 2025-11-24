package ua.edu.ukma.event_management_micro.user.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String setSecretString;
	private byte[] secretKey;


	@PostConstruct
	public void init() {
		if (setSecretString != null && !setSecretString.isBlank()) {
			this.secretKey = setSecretString.getBytes();
		}
	}

	public JwtService() throws NoSuchAlgorithmException {
		if (setSecretString != null && !setSecretString.isBlank()) {
			this.secretKey = setSecretString.getBytes();
			return;
		}
		KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
		SecretKey key = keyGenerator.generateKey();
		this.secretKey = key.getEncoded();
	}
	public String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}

	public boolean validate(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	public boolean validateServer(String token) {
		String username = extractUsername(token);
		return username.equals("api") && !isTokenExpired(token);
	}


	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();

		claims.put("roles", List.of("API_SERVER"));

		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
				.and()
				.signWith(Keys.hmacShaKeyFor(secretKey))
				.compact();
	}

	private boolean isTokenExpired(String token) {
		return extractClaims(token)
				.getExpiration()
				.before(new Date());
	}

	private Claims extractClaims(String token) {
		return Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(secretKey))
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

}
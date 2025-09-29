package ua.edu.ukma.event_management_micro.user.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

	private UserDetailsService userDetailsService;
	private final byte[] secretKey;

	@Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public JwtService() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
			SecretKey key = keyGenerator.generateKey();
			this.secretKey = key.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	public String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}

	public boolean validate(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}


	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();

		claims.put("roles", userDetailsService.loadUserByUsername(username)
				.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList());

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

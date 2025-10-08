package ua.edu.ukma.event_management_micro.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private JwtService jwtService;
	private PasswordEncoder passwordEncoder;

	@Value("${secret.pass}")
	private String secretPass;

	private String secretHash;
//	private UserDetailsService userDetailsService;


	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	@Autowired
	public void setJwtService(JwtService jwtService) {
		this.jwtService = jwtService;
	}

//	@Autowired
//	public void setUserDetailsService(UserDetailsService userDetailsService) {
//		this.userDetailsService = userDetailsService;
//	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		System.out.printf("TEST STRING AHAHHAHAA");

		try {
			if (authHeader != null
					&& !authHeader.isBlank()
					&& authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
			} else if (request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					if ("jwtToken".equals(cookie.getName())) {
						token = cookie.getValue();
						break;
					}
				}
			}
			username = jwtService.extractUsername(token);
		} catch (Exception e) {
			filterChain.doFilter(request, response);
			return;
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			if (jwtService.validateServer(token)) {

				if (secretHash == null) {
					secretHash = passwordEncoder.encode(secretPass);
				}

				UserDetails userDetails = new UserDetails() {
					@Override
					public Collection<? extends GrantedAuthority> getAuthorities() {
						return List.of(() -> "API_SERVER");
					}

					@Override
					public String getPassword() {
						return secretHash;
					}

					@Override
					public String getUsername() {
						return "api";
					}
				};


				UsernamePasswordAuthenticationToken userPassToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				userPassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(userPassToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}

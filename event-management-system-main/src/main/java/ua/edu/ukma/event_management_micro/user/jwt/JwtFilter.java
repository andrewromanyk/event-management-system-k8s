package ua.edu.ukma.event_management_micro.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private JwtService jwtService;

	private UserDetailsService userDetailsService;

	@Autowired
	public void setJwtService(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		try {
			if (authHeader != null
					&& !authHeader.isBlank()
					&& authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
				username = jwtService.extractUsername(token);
			} else if (request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					if ("jwtToken".equals(cookie.getName())) {
						token = cookie.getValue();
						break;
					}
				}
				username = jwtService.extractUsername(token);
			}
		} catch (Exception e) {
			filterChain.doFilter(request, response);
			return;
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (jwtService.validate(token, userDetails)) {
				UsernamePasswordAuthenticationToken userPassToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				userPassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(userPassToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}

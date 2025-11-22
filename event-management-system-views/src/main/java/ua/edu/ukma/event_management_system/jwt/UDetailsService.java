package ua.edu.ukma.event_management_system.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.edu.ukma.event_management_system.clients.UserClient;
import ua.edu.ukma.event_management_system.dto.UserDto;

@Service
public class UDetailsService implements UserDetailsService {

	UserClient userClient;

	@Autowired
	public UDetailsService(UserClient userClient) {
		this.userClient = userClient;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto user = userClient.getUser(username).getBody();

		if (user == null) {
			throw new UsernameNotFoundException("User %s was not found".formatted(username));
		}

		return new UserPrincipal(user);
	}
}

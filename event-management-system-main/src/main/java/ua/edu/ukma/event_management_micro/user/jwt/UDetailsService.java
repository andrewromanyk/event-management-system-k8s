package ua.edu.ukma.event_management_micro.user.jwt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.edu.ukma.event_management_micro.user.UserEntity;
import ua.edu.ukma.event_management_micro.user.UserRepository;

@Service
public class UDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User %s was not found".formatted(username));
		}

		return new UserPrincipal(user);
	}
}

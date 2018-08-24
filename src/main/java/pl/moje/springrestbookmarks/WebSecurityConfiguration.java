package pl.moje.springrestbookmarks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfiguration {

	@Autowired
	private AccountRepository accountRepository;
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	UserDetailsService userDetailsService() {
		return (username) -> accountRepository
								.findByUsername(username)
								.map(a -> User.builder()
											.username(a.getUsername())
											.password(a.getPassword())
											.authorities("USER", "write")
											.build())
								.orElseThrow(() -> new UsernameNotFoundException("Could not find username " + username));
	}
}

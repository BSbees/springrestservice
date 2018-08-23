package pl.moje.springrestbookmarks;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String userId) {
		super("Could not find user " + userId);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1019402860275213706L;
	
}

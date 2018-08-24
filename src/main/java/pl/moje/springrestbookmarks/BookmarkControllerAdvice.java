package pl.moje.springrestbookmarks;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookmarkControllerAdvice {

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	VndErrors userNotFountExceptionHandler(UserNotFoundException e) {
		return new VndErrors("error", e.getMessage());
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(BookmarkNotFoundException.class)
	VndErrors bookmarkNotFoundExceptionHandler(BookmarkNotFoundException e) {
		return new VndErrors("error", e.getMessage());
	}
}

package pl.moje.springrestbookmarks;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("{userId}/bookmarks")
public class BookmarkRestController {

	private final AccountRepository accountRepository;
	private final BookmarkRepository bookmarkRepository;
	
	@Autowired
	public BookmarkRestController(AccountRepository accountRepository, BookmarkRepository bookmarkRepository) {
		this.accountRepository = accountRepository;
		this.bookmarkRepository = bookmarkRepository;
	}
	
	@GetMapping
	Collection<Bookmark> readBookmarks(@PathVariable String userId){
		validateUser(userId);
		return this.bookmarkRepository.findByAccountUsername(userId);
	}
	
	@PostMapping
	ResponseEntity<?> add(@PathVariable String userId, @RequestBody Bookmark input) {
		this.validateUser(userId);	
		
		return this.accountRepository
				.findByUsername(userId)
				.map(account->{
					Bookmark result = this.bookmarkRepository.save(
							new Bookmark(account, input.getURI(), input.getDescription()));
					URI location = ServletUriComponentsBuilder
							.fromCurrentRequest()
							.buildAndExpand(result.getId())
							.toUri();
					
					return ResponseEntity.created(location).build();
				}).orElse(ResponseEntity.noContent().build());
	}
	
	@GetMapping("/{bookmarkId}")
	Bookmark readBookmark(@PathVariable String userId, @PathVariable Long bookmarkId) {
		this.validateUser(userId);
		
		return this.bookmarkRepository
				.findById(bookmarkId)
				.orElseThrow(() -> new BookmarkNotFoundException(bookmarkId));
	}

	private void validateUser(String userId) {
		this.accountRepository.findByUsername(userId)
			.orElseThrow(()->new UserNotFoundException(userId));
	}
}

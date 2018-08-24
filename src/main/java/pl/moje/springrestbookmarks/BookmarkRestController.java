package pl.moje.springrestbookmarks;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookmarks")
class BookmarkRestController {

	private final BookmarkRepository bookmarkRepository;

	private final AccountRepository accountRepository;

	BookmarkRestController(BookmarkRepository bookmarkRepository,
						   AccountRepository accountRepository) {
		this.bookmarkRepository = bookmarkRepository;
		this.accountRepository = accountRepository;
	}

	@GetMapping
	Resources<Resource<Bookmark>> readBookmarks(Principal principal) {
		this.validateUser(principal);

		List<Resource<Bookmark>> bookmarkResourceList = bookmarkRepository
			.findByAccountUsername(principal.getName()).stream()
			.map(bookmark -> toResource(bookmark, principal))
			.collect(Collectors.toList());

		return new Resources<>(bookmarkResourceList);
	}

	@PostMapping
	ResponseEntity<?> add(Principal principal, @RequestBody Bookmark input) {
		this.validateUser(principal);

		return accountRepository
			.findByUsername(principal.getName())
			.map(account -> ResponseEntity.created(
				URI.create(
					toResource(
						this.bookmarkRepository.save(Bookmark.from(account, input)), principal)
						.getLink(Link.REL_SELF).getHref()))
				.build())
			.orElse(ResponseEntity.noContent().build());
	}

	@GetMapping("/{bookmarkId}")
	Resource<Bookmark> readBookmark(Principal principal, @PathVariable Long bookmarkId) {
		this.validateUser(principal);

		return this.bookmarkRepository.findById(bookmarkId)
			.map(bookmark -> toResource(bookmark, principal))
			.orElseThrow(() -> new BookmarkNotFoundException(bookmarkId));
	}

	private void validateUser(Principal principal) {
		this.accountRepository
			.findByUsername(principal.getName())
			.orElseThrow(
				() -> new UserNotFoundException(principal.getName()));
	}

	/**
	 * Transform a {@link Bookmark} into a {@link Resource}.
	 *
	 * @param bookmark
	 * @param principal
	 * @return
	 */
	private static Resource<Bookmark> toResource(Bookmark bookmark, Principal principal) {
		return new Resource(bookmark,

			// Create a raw link using a URI and a rel
			new Link(bookmark.getURI(), "bookmark-uri"),

			// Create a link to a the collection of bookmarks associated with the user
			linkTo(methodOn(BookmarkRestController.class).readBookmarks(principal)).withRel("bookmarks"),

			// Create a "self" link to a single bookmark
			linkTo(methodOn(BookmarkRestController.class).readBookmark(principal, bookmark.getId())).withSelfRel());
	}
}
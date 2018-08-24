package pl.moje.springrestbookmarks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Bookmark {

	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	private Account account;
	
	private String URI;
	
	private String description;
	
	private Bookmark() {}

	public Bookmark(Account account, String uRI, String description) {
		super();
		this.account = account;
		URI = uRI;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public Account getAccount() {
		return account;
	}

	public String getURI() {
		return URI;
	}

	public String getDescription() {
		return description;
	}

	public static Bookmark from(Account account, Bookmark bookmark) {
		return new Bookmark(account, bookmark.URI, bookmark.getDescription());
	}
	
}

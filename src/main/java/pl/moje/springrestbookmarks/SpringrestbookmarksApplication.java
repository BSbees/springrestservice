package pl.moje.springrestbookmarks;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class SpringrestbookmarksApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringrestbookmarksApplication.class, args);
	}
	
	@Bean
	@Order(1)
	CommandLineRunner init(BookmarkRepository bookmarkRepository, AccountRepository accountRepository) {
		return args -> {
			Arrays.asList("jhoeller","dsyer","pwebb","ogierke","rwinch","mfisher","mpollack","jlong")
				.forEach(username -> {
					Account account = accountRepository.save(new Account(username, "password"));
					bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/1/" + username, "A description"));
					bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/2/" + username, "A description"));
			});
		};
	}
	
	@Bean
	@Order(2)
	CommandLineRunner init2(BookmarkRepository bookmarkRepository, AccountRepository accountRepository) {
		return args -> {
			System.out.println("Bookmark db:");
			System.out.println(bookmarkRepository.findAll());
			System.out.println("Account db");
			System.out.println(accountRepository.findAll());
		};
	}
}

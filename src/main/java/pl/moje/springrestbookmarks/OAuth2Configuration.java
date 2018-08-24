package pl.moje.springrestbookmarks;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
@EnableResourceServer
@EnableAuthorizationServer
class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

	String applicationName = "bookmarks";

	// This is required for password grants, which we specify below as one of the {@literal authorizedGrantTypes()}.
	final AuthenticationManagerBuilder authenticationManager;

	public OAuth2Configuration(AuthenticationManagerBuilder authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		// Workaround for https://github.com/spring-projects/spring-boot/issues/1801
		endpoints.authenticationManager(authentication -> authenticationManager.getOrBuild().authenticate(authentication));
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory()
			.withClient("android-" + applicationName)
			.authorizedGrantTypes("password", "authorization_code", "refresh_token")
			.authorities("ROLE_USER")
			.scopes("write")
			.resourceIds(applicationName)
			.secret("123456");
	}
}

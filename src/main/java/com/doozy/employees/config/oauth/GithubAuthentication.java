package com.doozy.employees.config.oauth;

import com.doozy.employees.authentication.LoginSuccessHandler;
import com.doozy.employees.config.SecurityConfiguration;
import com.doozy.employees.model.Provider;
import com.doozy.employees.model.Role;
import com.doozy.employees.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

@Component
@Configuration
@OAuthProvider
public class GithubAuthentication extends AbstractHttpConfigurer<UrlAuthorizationConfigurer<HttpSecurity>, HttpSecurity> {

	private static final String GITHUB_LOGIN = "/login/github";
	private OAuth2ClientContext oauth2ClientContext;

	private final ApplicationEventPublisher publisher;

	private final RoleService mRoleService;

	@Autowired
	public GithubAuthentication(@Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext, ApplicationEventPublisher publisher, RoleService roleService) {
		this.oauth2ClientContext = oauth2ClientContext;
		this.publisher = publisher;
		mRoleService = roleService;
	}

	@Override
	public void configure(HttpSecurity http) {
		http.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
	}

	private Filter ssoFilter() {
		SecurityConfiguration.ClientResources github = github();
		return SecurityConfiguration.ssoFilter(oauth2ClientContext, github, GITHUB_LOGIN, successHandler());
	}

	@Bean
	@ConfigurationProperties("github")
	public SecurityConfiguration.ClientResources github() {
		return new SecurityConfiguration.ClientResources();
	}

	@Bean
	LoginSuccessHandler successHandler() {
		return new LoginSuccessHandler(Provider.GITHUB, mRoleService.findById(3L).orElse(null), publisher);
	}
}

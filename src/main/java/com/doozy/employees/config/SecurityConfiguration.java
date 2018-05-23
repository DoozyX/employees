package com.doozy.employees.config;

import com.doozy.employees.config.oauth.OAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableOAuth2Client
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;

	private final OAuth2ClientContext oauth2ClientContext;

	private final ApplicationContext applicationContext;

	@Autowired
	public SecurityConfiguration(@Qualifier("employee") UserDetailsService userDetailsService, @Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext, ApplicationContext applicationContext) {
		this.userDetailsService = userDetailsService;
		this.oauth2ClientContext = oauth2ClientContext;
		this.applicationContext = applicationContext;
	}

	public static Filter ssoFilter(OAuth2ClientContext oauth2ClientContext, ClientResources client, String path, AuthenticationSuccessHandler successHandler) {
		OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter =
				new OAuth2ClientAuthenticationProcessingFilter(path);

		OAuth2RestTemplate oAuth2RestTemplate =
				new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);

		oAuth2ClientAuthenticationFilter.setRestTemplate(oAuth2RestTemplate);

		UserInfoTokenServices tokenServices = new UserInfoTokenServices(
				client.getResource().getUserInfoUri(),
				client.getClient().getClientId()
		);

		tokenServices.setRestTemplate(oAuth2RestTemplate);

		oAuth2ClientAuthenticationFilter.setTokenServices(tokenServices);

		oAuth2ClientAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);

		return oAuth2ClientAuthenticationFilter;
	}

	@Bean
	@Order(0)
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	public static class ClientResources {

		@NestedConfigurationProperty
		private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

		@NestedConfigurationProperty
		private ResourceServerProperties resource = new ResourceServerProperties();

		public AuthorizationCodeResourceDetails getClient() {
			return client;
		}

		public ResourceServerProperties getResource() {
			return resource;
		}
	}


	@Override
	protected void configure(
			AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.requiresChannel()
				.anyRequest();


		http.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/profile", true)
				.permitAll();


		http.rememberMe().key("uniqueRememberMeKey").tokenValiditySeconds(2592000)
				.and()
				.logout().clearAuthentication(true).invalidateHttpSession(true).deleteCookies("remember-me")
				.and()
				.authorizeRequests().antMatchers("/api/*").hasAuthority("ADMIN")
				.and()
				.authorizeRequests().antMatchers("/department/**").hasAuthority("ADMIN")
				.and()
				.authorizeRequests().antMatchers("/department/**").hasAuthority("MANAGER")
				.and()
				.authorizeRequests().antMatchers("/employee/**").hasAuthority("ADMIN")
				.and()
				.authorizeRequests().antMatchers("/employee/**").hasAuthority("MANAGER")
				.and()
				.authorizeRequests().antMatchers("/*").permitAll();

		//http.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
		List<SecurityConfigurerAdapter> additionalOAuthProviders = fetchAdditionalOAuthConfigurations();
		for (SecurityConfigurerAdapter provider : additionalOAuthProviders) {
			http.apply(provider);
		}

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}

//	@Bean
//	@ConfigurationProperties("facebook")
//	public ClientResources facebook() {
//		return new ClientResources();
//	}

//	private Filter ssoFilter() {
//		CompositeFilter filter = new CompositeFilter();
//		List<Filter> filters = new ArrayList<>();
//		filters.add(ssoFilter(oauth2ClientContext, facebook(), "/login/facebook", null));
//		filter.setFilters(filters);
//		return filter;
//	}

	private List<SecurityConfigurerAdapter> fetchAdditionalOAuthConfigurations() {
		return applicationContext.getBeansWithAnnotation(OAuthProvider.class)
				.values().stream().map(i -> (SecurityConfigurerAdapter) i).collect(Collectors.toList());
	}

}

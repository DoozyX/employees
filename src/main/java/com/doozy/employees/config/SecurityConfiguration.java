package com.doozy.employees.config;

import com.doozy.employees.service.impl.EmployeeDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

@Configuration
//@EnableOAuth2Client
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


	@Bean
	@Order(0)
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	private final UserDetailsService userDetailsService;

	//@Qualifier("oauth2ClientContext")
	//@Autowired
	//OAuth2ClientContext oauth2ClientContext;

	@Autowired
	public SecurityConfiguration(@Qualifier("employee") UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
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

		http.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/profile", true)
				.permitAll();


		http.rememberMe().key("uniqueRememberMeKey").tokenValiditySeconds(2592000)
				.and()
				.logout().clearAuthentication(true).invalidateHttpSession(true).deleteCookies("remember-me")
				//.and()
				//.authorizeRequests().antMatchers("/api/*", "/me", "/profile", "/employee/create").hasRole("ADMIN")
				//.and()
				//.authorizeRequests().antMatchers("/edit-profile-change-password").hasAuthority("CHANGE_PASSWORD_AUTHORITY")
				.and()
				.authorizeRequests().antMatchers("/*").permitAll();

		//http.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//@Bean
	//public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
	//  FilterRegistrationBean registration = new FilterRegistrationBean();
	//  registration.setFilter(filter);
	//  registration.setOrder(-100);
	//  return registration;
	//}
//
//  @Bean
//  @ConfigurationProperties("github")
//  public ClientResources github() {
//    return new ClientResources();
//  }
//
//  @Bean
//  @ConfigurationProperties("google")
//  public ClientResources google() {
//    return new ClientResources();
//  }
//
//  @Bean
//  @ConfigurationProperties("facebook")
//  public ClientResources facebook() {
//    return new ClientResources();
//  }
////
////  private Filter ssoFilter() {
////    CompositeFilter filter = new CompositeFilter();
////    List<Filter> filters = new ArrayList<>();
////    filters.add(ssoFilter(facebook(), "/login/facebook"));
//    filters.add(ssoFilter(github(), "/login/github"));
//    filters.add(ssoFilter(google(), "/login/google"));
//    filter.setFilters(filters);
//    return filter;
//  }
//
//  private Filter ssoFilter(ClientResources client, String path) {
//    OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter = new OAuth2ClientAuthenticationProcessingFilter(path);
//    OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
//    oAuth2ClientAuthenticationFilter.setRestTemplate(oAuth2RestTemplate);
//    UserInfoTokenServices tokenServices = new UserInfoTokenServices(client.getResource().getUserInfoUri(),
//      client.getClient().getClientId());
//    tokenServices.setRestTemplate(oAuth2RestTemplate);
//    oAuth2ClientAuthenticationFilter.setTokenServices(tokenServices);
//    return oAuth2ClientAuthenticationFilter;
//  }
//
//  class ClientResources {
//
//    @NestedConfigurationProperty
//    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();
//
//    @NestedConfigurationProperty
//    private ResourceServerProperties resource = new ResourceServerProperties();
//
//    public AuthorizationCodeResourceDetails getClient() {
//      return client;
//    }
//
//    public ResourceServerProperties getResource() {
//      return resource;
//    }
//  }

}

package com.doozy.employees.authentication;

import com.doozy.employees.model.Employee;
import com.doozy.employees.model.Provider;
import com.doozy.employees.model.Role;
import com.doozy.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginSuccessHandler  extends SavedRequestAwareAuthenticationSuccessHandler {

	ApplicationEventPublisher publisher;
	private Provider provider;
	private Role defaultUserType;
	@Autowired
	private EmployeeService mEmployeeService;

	private Employee user;

	public LoginSuccessHandler(Provider provider, Role defaultUserType, ApplicationEventPublisher publisher) {
		this.provider = provider;
		this.defaultUserType = defaultUserType;
		this.publisher = publisher;
	}

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Authentication authentication
	) throws IOException, ServletException {

		HttpSession session = httpServletRequest.getSession();
		Employee user = getUser(authentication);
		session.setAttribute("user", user);

		super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
	}

	public Employee getUser(Authentication authentication) {
		Employee user = mEmployeeService.findByEmail(authentication.getName()).orElse(null);
		if (user == null) {
			user = createUserFromProvider(authentication);
		}
		return user;
	}

	private Employee createUserFromProvider(Authentication authentication) {
		user = new Employee();
		user.email = authentication.getName() + "@doozy.com";
		user.role = defaultUserType;
		user.provider = provider;
		try {
			mEmployeeService.save(user);
		}catch (Exception e) {
			e.printStackTrace();
		}


		return user;
	}
}

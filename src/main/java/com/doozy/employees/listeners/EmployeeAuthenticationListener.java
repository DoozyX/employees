package com.doozy.employees.listeners;

import com.doozy.employees.events.EmployeePasswordResetEvent;
import com.doozy.employees.events.EmployeeRegisteredEvent;
import com.doozy.employees.model.Employee;
import com.doozy.employees.service.EmployeeService;
import com.doozy.employees.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmployeeAuthenticationListener {
	private final EmployeeService mEmployeeService;
	private final MailService mMailService;

	@Autowired
	public EmployeeAuthenticationListener(EmployeeService mEmployeeService, MailService mMailService) {
		this.mEmployeeService = mEmployeeService;
		this.mMailService = mMailService;
	}

	@EventListener
	public void onApplicationEvent(EmployeeRegisteredEvent event) {
		Employee employee = event.getEmployee();
		String randomToken = UUID.randomUUID().toString();

		mEmployeeService.setVerificationToken(employee, randomToken);

		String confirmationURL = "http://localhost:8080/registrationConfirm?token=" + randomToken;
		mMailService.sendEmail(employee.getEmail(), "Activate your account", "Activate your account using the following url" + " \n" + confirmationURL);

	}

	@EventListener
	public void onApplicationEvent(EmployeePasswordResetEvent event) {
		Employee employee = event.getEmployee();
		String randomToken = UUID.randomUUID().toString();

		mEmployeeService.setVerificationToken(employee, randomToken);

		String url = "http://localhost:8080/change-password?id=" +
				employee.getId() + "&token=" + randomToken;
		String message = "Reset your password\r\n" + url;
		mMailService.sendEmail(employee.getEmail(), "Reset Password", message);
		String confirmationURL = "http://localhost:8080/registrationConfirm?token=" + randomToken;
		mMailService.sendEmail(employee.getEmail(), "Activate your account", "Activate your account using the following url" + " \n" + confirmationURL);

	}

}

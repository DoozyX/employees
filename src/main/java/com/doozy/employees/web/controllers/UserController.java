package com.doozy.employees.web.controllers;

import com.doozy.employees.config.thymeleaf.Layout;
import com.doozy.employees.model.Employee;
import com.doozy.employees.model.exceptions.CaptchaException;
import com.doozy.employees.model.exceptions.UserNotFoundException;
import com.doozy.employees.service.CaptchaService;
import com.doozy.employees.service.EmployeeService;
import com.doozy.employees.web.dto.EmployeeDto;
import com.doozy.employees.web.dto.EmployeeVerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.doozy.employees.service.impl.EmployeeServiceImpl.VALID_TOKEN;

@Controller
public class UserController {

	private final CaptchaService captchaService;

	private final EmployeeService mEmployeeService;

	private final Environment mEnvironment;

	private final JavaMailSender mJavaMailSender;

	@Autowired
	public UserController(CaptchaService captchaService, EmployeeService employeeService, Environment environment, JavaMailSender mailSender) {
		this.captchaService = captchaService;
		this.mEmployeeService = employeeService;
		this.mEnvironment = environment;
		this.mJavaMailSender = mailSender;
	}

	@Layout("layouts/master")
	@GetMapping("/login")
	public String login() {
		return "fragments/login";
	}

	@Layout("layouts/master")
	@GetMapping(value = "/register")
	public String showRegistrationForm(Model model) {
		EmployeeDto employeeDto = new EmployeeDto();
		model.addAttribute("user", employeeDto);
		return "fragments/register";
	}

	@Layout("layouts/master")
	@PostMapping(value = "/register")
	public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid final EmployeeDto employeeDto, BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("validationErrors", bindingResult.getAllErrors());
			if (!employeeDto.getPassword().equals(employeeDto.getMatchingPassword())) {
				model.addAttribute("passNoMatch", "Passwords don't match!");
			}
			model.addAttribute("user", employeeDto);
			return new ModelAndView("fragments/register", "user", employeeDto);
		} else {

			try {
				String response = httpServletRequest.getParameter("g-recaptcha-response");
				captchaService.processResponse(response, getClientIP(httpServletRequest));
			} catch (CaptchaException e) {
				return new ModelAndView("fragments/register", "user", employeeDto);
			}

			Employee registered = mEmployeeService.registerNewEmployee(employeeDto);

			if (registered == null) {
				bindingResult.rejectValue("email", "message.regError");
			}

			if (bindingResult.hasErrors()) {
				return new ModelAndView("fragments/register", "user", employeeDto);
			} else {
				String appUrl = httpServletRequest.getProtocol() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort();
				mEmployeeService.createAndSendVerificationToken(registered, appUrl);

				model.addAttribute("user", registered);
				assert registered != null;
				return new ModelAndView("fragments/confirmation-email", "user", registered);
			}

		}
	}

	@GetMapping(value = "/registrationConfirm")
	public String confirmRegistration(Model model, @RequestParam("token") String token) {

		EmployeeVerificationToken verificationToken = mEmployeeService.getToken(token);
		if (verificationToken == null) {
			model.addAttribute("message", "Invalid token");
			return "redirect:/badUser";
		}

		if (mEmployeeService.validateVerificationToken(token).equals(VALID_TOKEN)) {
			return "redirect:/login";
		} else {
			model.addAttribute("message", "Invalid");
			return "redirect:/badUser";
		}
	}

	@Layout("layouts/master")
	@GetMapping(value = "/reset-password")
	public String resetPassword(@RequestParam(required = false) String email, Model model) {
		if (email != null) {

			Optional<Employee> user = mEmployeeService.findByEmail(email);
			if (!user.isPresent()) {
				throw new UserNotFoundException();
			}

			String token = UUID.randomUUID().toString();
			mEmployeeService.createPasswordResetTokenForEmployee(user.get(), token);

			// Send reset password email
			mJavaMailSender.send(constructResetTokenEmail("http://localhost:8080", token, user.get()));

			model.addAttribute("confirmationEmail", email);
		}
		return "fragments/reset-password";
	}

	@GetMapping(value = "/change-password")
	public String showChangePasswordPage(@RequestParam("id") final Long id, @RequestParam("token") final String token) {
		final String result = mEmployeeService.validatePasswordResetToken(id, token);
		if (result != null) {
			return "redirect:/login";
		}
		return "redirect:/edit-profile-change-password";
	}

	@Layout("layouts/master")
	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {

		String userName = principal.getName();
		Employee u = mEmployeeService.findByUsername(userName);

		model.addAttribute("user", u);
		return "fragments/user-profile";
	}

	@Layout("layouts/master")
	@GetMapping(value = "/edit-profile-change-password")
	public String changeProfilePassword() {
		return "fragments/edit-profile-change-password";
	}

	@Layout("layouts/master")
	@PostMapping(value = "/edit-profile-change-password")
	public String proceedChangeProfilePassword(@RequestParam Map<String, String> passwordDto) {
		Employee user = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		mEmployeeService.changeUserPassword(user, passwordDto.get("password"));
		return "redirect:/login";
	}

	private String getClientIP(HttpServletRequest request) {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}

	private SimpleMailMessage constructResetTokenEmail(String contextPath, String token, Employee user) {
		String url = contextPath + "/change-password?id=" +
				user.getId() + "&token=" + token;
		String message = "Reset your password";
		return constructEmail(message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String body, Employee user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject("Reset Password");
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(Objects.requireNonNull(mEnvironment.getProperty("from.email")));
		return email;
	}

}

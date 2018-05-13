package com.doozy.employees.web.controllers;

import com.doozy.employees.config.thymeleaf.Layout;
import com.doozy.employees.model.Employee;
import com.doozy.employees.model.exceptions.CaptchaException;
import com.doozy.employees.model.exceptions.UserNotFoundException;
import com.doozy.employees.service.CaptchaService;
import com.doozy.employees.service.EmployeeService;
import com.doozy.employees.web.dto.RegisterEmployeeDto;
import com.doozy.employees.model.EmployeeVerificationToken;
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
public class LoginController {

	private final CaptchaService captchaService;

	private final EmployeeService mEmployeeService;

	private final Environment mEnvironment;

	private final JavaMailSender mJavaMailSender;

	@Autowired
	public LoginController(CaptchaService captchaService, EmployeeService employeeService, Environment environment, JavaMailSender mailSender) {
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
		RegisterEmployeeDto registerEmployeeDto = new RegisterEmployeeDto();
		model.addAttribute("employee", registerEmployeeDto);
		return "fragments/register";
	}

	@Layout("layouts/master")
	@PostMapping(value = "/register")
	public ModelAndView registerUserAccount(@ModelAttribute("employee") @Valid final RegisterEmployeeDto registerEmployeeDto, BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("validationErrors", bindingResult.getAllErrors());
			if (!registerEmployeeDto.getPassword().equals(registerEmployeeDto.getMatchingPassword())) {
				model.addAttribute("passNoMatch", "Passwords don't match!");
			}
			model.addAttribute("user", registerEmployeeDto);
			return new ModelAndView("fragments/register", "user", registerEmployeeDto);
		} else {
			try {
				String response = httpServletRequest.getParameter("g-recaptcha-response");
				captchaService.processResponse(response, getClientIP(httpServletRequest));
			} catch (CaptchaException e) {
				return new ModelAndView("fragments/register", "user", registerEmployeeDto);
			}

			Employee registered = mEmployeeService.registerNewEmployee(registerEmployeeDto);

			if (registered == null) {
				bindingResult.rejectValue("email", "message.regError");
			}

			if (bindingResult.hasErrors()) {
				return new ModelAndView("fragments/register", "user", registerEmployeeDto);
			} else {
				mEmployeeService.createAndSendVerificationToken(registered);

				model.addAttribute("user", registered);
				assert registered != null;
				return new ModelAndView("fragments/confirmation-email", "user", registered);
			}

		}
	}

	@Layout("layouts/master")
	@GetMapping(value = "/registrationConfirm")
	public String confirmRegistration(Model model, @RequestParam("token") String token) {

		EmployeeVerificationToken verificationToken = mEmployeeService.getToken(token);
		if (verificationToken == null) {
			model.addAttribute("message", "Invalid token");
			return "redirect:/badUser";
		}

		if (mEmployeeService.validateVerificationToken(token).equals(VALID_TOKEN)) {
			return "fragments/successful-confirmation";
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

			mEmployeeService.createAndSendPasswordResetTokenForEmployee(user.get());

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
		Optional<Employee> user = mEmployeeService.findByEmail(userName);
		if (!user.isPresent()) {
			throw new UserNotFoundException();
		}

		model.addAttribute("user", user.get());
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
		return "redirect:/";
	}

	private String getClientIP(HttpServletRequest request) {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}


}

package com.doozy.employees.service.impl;

import com.doozy.employees.events.EmployeePasswordResetEvent;
import com.doozy.employees.events.EmployeeRegisteredEvent;
import com.doozy.employees.model.Employee;
import com.doozy.employees.model.exceptions.DuplicateEmailException;
import com.doozy.employees.persistance.EmployeeRepository;
import com.doozy.employees.persistance.PasswordResetTokenRepository;
import com.doozy.employees.persistance.RoleRepository;
import com.doozy.employees.persistance.VerificationTokenRepository;
import com.doozy.employees.service.EmployeeService;
import com.doozy.employees.model.EmployeeVerificationToken;
import com.doozy.employees.model.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	private static final String INVALID_TOKEN = "invalidToken";
	private static final String EXPIRED_TOKEN = "expired";
	public static final String VALID_TOKEN = "valid";

	private final ApplicationEventPublisher mApplicationEventPublisher;

	private final EmployeeRepository mEmployeeRepository;
	private final RoleRepository mRoleRepository;

	private final PasswordEncoder mPasswordEncoder;
	private final VerificationTokenRepository mVerificationTokenRepository;
	private final PasswordResetTokenRepository mPasswordResetTokenRepository;

	@Autowired
	public EmployeeServiceImpl(ApplicationEventPublisher applicationEventPublisher, EmployeeRepository employeeRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
	                           VerificationTokenRepository verificationTokenRepository, PasswordResetTokenRepository passwordResetTokenRepository) {
		mApplicationEventPublisher = applicationEventPublisher;
		mEmployeeRepository = employeeRepository;
		mRoleRepository = roleRepository;
		mPasswordEncoder = passwordEncoder;
		mVerificationTokenRepository = verificationTokenRepository;
		mPasswordResetTokenRepository = passwordResetTokenRepository;
	}

	@Override
	public Optional<Employee> findById(Long id) {
		return Optional.of(mEmployeeRepository.findById(id)).orElse(Optional.empty());
	}

	@Override
	@Deprecated
	public Collection<Employee> findAll() {
		return mEmployeeRepository.findAll();
	}

	@Override
	public Page<Employee> findAll(Pageable pageable) {
		return mEmployeeRepository.findAll(pageable);
	}

	@Override
	public Employee save(Employee employee) {
		if (employee.getRegistration() == null) {

			boolean sendNewPassword = false;
			if (this.checkDuplicateEmail(employee.getEmail())) {
				throw new DuplicateEmailException();
			}

			if(employee.getPassword() == null) {
				employee.setPassword("notSet");
				sendNewPassword = true;
			}

			employee.setPassword(mPasswordEncoder.encode(employee.getPassword()));
			if (mRoleRepository.findById(3L).isPresent()) {
				employee.setRole(mRoleRepository.findById(3L).get());
			} else {
				throw new IllegalStateException();
			}

			employee.setActivated(false);
			employee.setRegistration(LocalDateTime.now().toString());
			mEmployeeRepository.save(employee);
			mApplicationEventPublisher.publishEvent(new EmployeeRegisteredEvent(employee));
			if (sendNewPassword) {
				createPasswordResetTokenForEmployee(employee);
			}
		} else {
			mEmployeeRepository.save(employee);
		}

		return employee;
	}

	@Override
	public void delete(Employee employee) {
		mEmployeeRepository.delete(employee);
	}

	@Override
	public Page<Employee> findByDepartmentId(Pageable pageable, Long departmentId) {
		return mEmployeeRepository.findByDepartmentId(pageable, departmentId);
	}

	@Override
	public void setVerificationToken(Employee employee, String token) {
		EmployeeVerificationToken employeeVerificationToken = new EmployeeVerificationToken();
		employeeVerificationToken.setToken(token);
		employeeVerificationToken.setUser(employee);
		mVerificationTokenRepository.save(employeeVerificationToken);
	}

	@Override
	public void createPasswordResetTokenForEmployee(Employee employee) {
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		String randomToken = UUID.randomUUID().toString();
		passwordResetToken.setToken(randomToken);
		passwordResetToken.setUser(employee);
		mPasswordResetTokenRepository.save(passwordResetToken);

		mApplicationEventPublisher.publishEvent(new EmployeePasswordResetEvent(employee));
	}

	@Override
	public Long count() {
		return mEmployeeRepository.count();
	}

	@Override
	public EmployeeVerificationToken getToken(String token) {
		return mVerificationTokenRepository.findByToken(token);
	}

	@Override
	public String validateVerificationToken(String token) {

		EmployeeVerificationToken userToken = mVerificationTokenRepository.findByToken(token);
		if (userToken == null) {
			return INVALID_TOKEN;
		}

		Employee user = userToken.getUser();
		Calendar calendar = Calendar.getInstance();
		if (userToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) {
			mVerificationTokenRepository.delete(userToken);
			return EXPIRED_TOKEN;
		}

		user.setActivated(true);
		mEmployeeRepository.save(user);
		return VALID_TOKEN;
	}

	@Override
	public Optional<Employee> findByEmail(String email) {
		return mEmployeeRepository.findByEmail(email);
	}


	@Override
	public String validatePasswordResetToken(Long id, String token) {
		PasswordResetToken passToken = mPasswordResetTokenRepository.findByToken(token);
		if ((passToken == null) || (!Objects.equals(passToken.getUser().getId(), id))) {
			return INVALID_TOKEN;
		}

		Calendar cal = Calendar.getInstance();
//    if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//      return EXPIRED_TOKEN;
//    }

		Employee employee = passToken.getUser();
		Authentication auth = new UsernamePasswordAuthenticationToken(employee, null, Collections.singletonList(new SimpleGrantedAuthority("CHANGE_PASSWORD_AUTHORITY")));
		SecurityContextHolder.getContext().setAuthentication(auth);
		return null;
	}

	@Override
	public void changeUserPassword(Employee user, String password) {
		user.setPassword(mPasswordEncoder.encode(password));
		mEmployeeRepository.save(user);
	}

	private boolean checkDuplicateEmail(final String email) {
		return mEmployeeRepository.findByEmail(email).isPresent();
	}
}
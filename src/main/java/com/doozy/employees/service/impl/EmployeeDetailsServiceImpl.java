package com.doozy.employees.service.impl;

import com.doozy.employees.model.Employee;
import com.doozy.employees.persistance.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author Riste Stojanov
 */
@Service("employee")
public class EmployeeDetailsServiceImpl implements UserDetailsService {

	private final EmployeeRepository mEmployeeRepository;

	@Autowired
	public EmployeeDetailsServiceImpl(EmployeeRepository employeeRepository) {
		this.mEmployeeRepository = employeeRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Employee> employeeOptional = mEmployeeRepository.findByEmail(email);
		if (!employeeOptional.isPresent()) {
			throw new UsernameNotFoundException("Not found");
		}
		Employee employee = employeeOptional.get();

		return new org.springframework.security.core.userdetails.User(
				employee.getUsername(),
				employee.getPassword(),
				employee.getAuthorities()
		);
	}
}

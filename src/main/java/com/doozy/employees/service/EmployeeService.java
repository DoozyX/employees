package com.doozy.employees.service;

import com.doozy.employees.model.Employee;
import com.doozy.employees.web.dto.RegisterEmployeeDto;
import com.doozy.employees.model.EmployeeVerificationToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeService extends BaseEntityService<Employee>{

	Page<Employee> findByDepartmentId(Pageable pageable, Long departmentId);

	Employee registerNewEmployee(RegisterEmployeeDto registerEmployeeDto);

	void createVerificationToken(Employee user);

	EmployeeVerificationToken getToken(String token);

	String validateVerificationToken(String token);

	Optional<Employee> findByEmail(String email);

	String validatePasswordResetToken(Long id, String token);

	void changeUserPassword(Employee user, String password);

	void createAndSendVerificationToken(Employee user);

	Employee saveAndGeneratePasswordAndSendMail(Employee employee);

	void createAndSendPasswordResetTokenForEmployee(Employee employee);
}

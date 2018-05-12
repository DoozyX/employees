package com.doozy.employees.web.dto;

import com.doozy.employees.model.Employee;
import com.doozy.employees.web.validators.PasswordMatches;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@PasswordMatches
@Getter
@Setter
public class EmployeeDto {
	@NotNull
	@NotEmpty
	public String email;

	@NotNull
	@NotEmpty
	public String password;

	public String matchingPassword;

	public String firstName;
	public String lastName;
	public Employee.Gender gender;
}

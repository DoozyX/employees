package com.doozy.employees.web.dto;

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
	public String username;

	@NotNull
	@NotEmpty
	public String password;

	public String matchingPassword;

	@NotNull
	@NotEmpty
	public String email;

}

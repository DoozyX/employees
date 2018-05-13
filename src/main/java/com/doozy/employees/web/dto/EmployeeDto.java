package com.doozy.employees.web.dto;

import com.doozy.employees.model.Department;
import com.doozy.employees.model.Employee;
import com.doozy.employees.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {
	public Long id;

	public String email;
	public String firstName;
	public String lastName;
	public Employee.Gender gender;
	public Department department;
	public String birthDate;
	public boolean activated;
	public String registration;
	public String activationCode;

	public Role role;

}
package com.doozy.employees.web.dto.mappers;

import com.doozy.employees.model.Employee;
import com.doozy.employees.web.dto.EmployeeDto;

public class EmployeeMapper {

	public static Employee mapDtoToEmployee(EmployeeDto dto, Employee employee) {
		employee.setEmail(dto.getEmail());
		employee.setFirstName(dto.getFirstName());
		employee.setLastName(dto.getLastName());
		employee.setGender(dto.getGender());
		employee.setDepartment(dto.getDepartment());
		employee.setBirthDate(dto.getBirthDate());
		employee.setActivated(dto.isActivated());
		employee.setRegistration(dto.getRegistration());
		employee.setActivationCode(dto.getActivationCode());
		employee.setRole(dto.getRole());

		return employee;
	}

	public static EmployeeDto mapEmployeeToDto(Employee employee) {
		EmployeeDto dto = new EmployeeDto();

		dto.setId(employee.getId());
		dto.setEmail(employee.getEmail());
		dto.setFirstName(employee.getFirstName());
		dto.setLastName(employee.getLastName());
		dto.setGender(employee.getGender());
		dto.setDepartment(employee.getDepartment());
		dto.setBirthDate(employee.getBirthDate());
		dto.setActivated(employee.isActivated());
		dto.setRegistration(employee.getRegistration());
		dto.setActivationCode(employee.getActivationCode());
		dto.setRole(employee.getRole());

		return dto;
	}
}
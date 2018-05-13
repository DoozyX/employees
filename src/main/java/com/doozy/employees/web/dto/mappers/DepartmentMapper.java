package com.doozy.employees.web.dto.mappers;

import com.doozy.employees.model.Department;
import com.doozy.employees.web.dto.DepartmentDto;


public class DepartmentMapper {
	public static Department mapDtoToDepartment(DepartmentDto dto, Department department) {
		department.setId(dto.getId());
		department.setName(dto.getName());

		return department;
	}

	public static DepartmentDto mapDepartmentToDto(Department department) {
		DepartmentDto dto = new DepartmentDto();

		dto.setId(department.getId());
		dto.setName(department.getName());

		return dto;
	}
}

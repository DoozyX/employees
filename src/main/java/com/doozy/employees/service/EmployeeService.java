package com.doozy.employees.service;

import com.doozy.employees.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService extends BaseEntityService<Employee>{
	Page<Employee> findByDepartmentId(Pageable pageable, Long departmentId);
}

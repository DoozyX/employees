package com.doozy.employees.persistance;


import com.doozy.employees.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeRepository extends BaseRepository<Employee, Long> {

	Page<Employee> findByDepartmentId(Pageable pageable, Long departmentId);
}
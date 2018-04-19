package com.doozy.employees.persistance.jpa;

import com.doozy.employees.model.Employee;
import com.doozy.employees.persistance.EmployeeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;

@Profile("jpa")
public interface JpaEmployeeRepository extends EmployeeRepository, Repository<Employee, Long> {
}

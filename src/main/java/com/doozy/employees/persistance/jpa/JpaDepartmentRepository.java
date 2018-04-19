package com.doozy.employees.persistance.jpa;

import com.doozy.employees.model.Department;
import com.doozy.employees.persistance.DepartmentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;

@Profile("jpa")
public interface JpaDepartmentRepository extends DepartmentRepository, Repository<Department, Long> {
}

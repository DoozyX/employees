package com.doozy.employees.persistance.mock;

import com.doozy.employees.model.Employee;
import com.doozy.employees.persistance.EmployeeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Profile("mock")
@Deprecated
@Component
public class MockEmployeeRepository implements EmployeeRepository {

	private static Map<Long, Employee> employeeMap = new HashMap<>();
	private static Long idSequencer = 0L;

	@Override
	public Collection<Employee> findAll() {
		return employeeMap.values();
	}

	@Override
	public Page<Employee> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public Optional<Employee> findById(Long id) {
		return Optional.ofNullable(employeeMap.get(id));
	}

	@Override
	public Employee save(Employee employee) {
		if (employee.getId() == null) {
			employee.setId(getNextIdValue());
		}
		employeeMap.put(employee.getId(), employee);
		return employee;
	}

	@Override
	public void delete(Employee employee) {
		if (employee != null) {
			employeeMap.remove(employee);
		}
	}

	private synchronized Long getNextIdValue() {
		return ++idSequencer;
	}
}
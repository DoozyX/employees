package com.doozy.employees.persistance.mock;

import com.doozy.employees.model.Department;
import com.doozy.employees.persistance.DepartmentRepository;
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
public class MockDepartmentRepository implements DepartmentRepository {

	private static Map<Long, Department> sDepartementMap = new HashMap<>();
	private static Long idSequencer = 0L;

	@Override
	public Collection<Department> findAll() {
		return sDepartementMap.values();
	}

	@Override
	public Page<Department> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public Optional<Department> findById(Long id) {
		return Optional.ofNullable(sDepartementMap.get(id));
	}

	@Override
	public Department save(Department employee) {
		if (employee.getId() == null) {
			employee.setId(getNextIdValue());
		}
		sDepartementMap.put(employee.getId(), employee);
		return employee;
	}

	@Override
	public void delete(Department department) {
		if (department != null) {
			sDepartementMap.remove(department);
		}
	}

	private synchronized Long getNextIdValue() {
		return ++idSequencer;
	}
}
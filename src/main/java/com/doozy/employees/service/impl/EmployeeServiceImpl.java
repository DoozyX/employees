package com.doozy.employees.service.impl;

import com.doozy.employees.model.Employee;
import com.doozy.employees.persistance.EmployeeRepository;
import com.doozy.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository mEmployeeRepository;

	@Autowired
	public EmployeeServiceImpl(EmployeeRepository mEmployeeRepository) {
		this.mEmployeeRepository = mEmployeeRepository;
	}

	@Override
	public Optional<Employee> findById(Long id) {
		return Optional.of(mEmployeeRepository.findById(id)).orElse(Optional.empty());
	}

	@Override
	public Collection<Employee> findAll() {
		return mEmployeeRepository.findAll();
	}

	@Override
	public Page<Employee> findAll(Pageable pageable) {
		return mEmployeeRepository.findAll(pageable);
	}

	@Override
	public Employee save(Employee employee) {
		employee.setActivated(false);
		employee.setRegistration(LocalDateTime.now());
		return mEmployeeRepository.save(employee);
	}

	@Override
	public void delete(Employee employee) {
		mEmployeeRepository.delete(employee);
	}


}
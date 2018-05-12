package com.doozy.employees.service.impl;


import com.doozy.employees.model.Department;
import com.doozy.employees.persistance.DepartmentRepository;
import com.doozy.employees.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository mDepartmentRepository;

	@Autowired
	public DepartmentServiceImpl(DepartmentRepository mDepartmentRepository) {
		this.mDepartmentRepository = mDepartmentRepository;
	}

	@Override
	public Optional<Department> findById(Long id) {
		return Optional.of(mDepartmentRepository.findById(id)).orElse(Optional.empty());
	}

	@Override
	public Collection<Department> findAll() {
		return mDepartmentRepository.findAll();
	}

	@Override
	public Page<Department> findAll(Pageable pageable) {
		return mDepartmentRepository.findAll(pageable);
	}

	@Override
	public Department save(Department department) {
		return mDepartmentRepository.save(department);
	}

	@Override
	public void delete(Department department) {
		mDepartmentRepository.delete(department);
	}

	@Override
	public Long count() {
		return mDepartmentRepository.count();
	}


}
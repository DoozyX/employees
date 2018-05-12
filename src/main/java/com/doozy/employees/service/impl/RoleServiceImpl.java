package com.doozy.employees.service.impl;

import com.doozy.employees.model.Employee;
import com.doozy.employees.model.Role;
import com.doozy.employees.persistance.RoleRepository;
import com.doozy.employees.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
	private final RoleRepository mRoleRepository;


	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		mRoleRepository = roleRepository;
	}


	@Override
	public Optional<Role> findById(Long id) {
		return mRoleRepository.findById(id);
	}

	@Override
	public Collection<Role> findAll() {
		return mRoleRepository.findAll();
	}

	@Override
	public Page<Role> findAll(Pageable pageable) {
		return mRoleRepository.findAll(pageable);
	}

	@Override
	public Role save(Role entity) {
		return mRoleRepository.save(entity);
	}

	@Override
	public void delete(Role entity) {
		mRoleRepository.delete(entity);

	}

	@Override
	public Long count() {
		return mRoleRepository.count();
	}
}

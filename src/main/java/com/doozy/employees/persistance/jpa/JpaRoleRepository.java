package com.doozy.employees.persistance.jpa;

import com.doozy.employees.model.Role;
import com.doozy.employees.persistance.RoleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;

@Profile("jpa")
public interface JpaRoleRepository extends RoleRepository, Repository<Role, Long> {
}

package com.doozy.employees.config.init;


import com.doozy.employees.model.Employee;
import com.doozy.employees.model.Role;
import com.doozy.employees.service.EmployeeService;
import com.doozy.employees.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FirstInit {
	private static Logger logger = LoggerFactory.getLogger(FirstInit.class);

	private final EmployeeService mEmployeeService;

	private final RoleService mRoleService;

	private final PasswordEncoder mPasswordEncoder;

	private final Environment mEnvironment;

	@Autowired
	public FirstInit(EmployeeService employeeService, RoleService roleService, PasswordEncoder passwordEncoder, Environment environment) {
		mEmployeeService = employeeService;
		mRoleService = roleService;
		mPasswordEncoder = passwordEncoder;
		mEnvironment = environment;
	}

	@PostConstruct
	public void init() {
		logger.debug("Initializing");

		if (mRoleService.count() == 0) {
			Role admin = new Role();
			admin.setName("ADMIN");
			mRoleService.save(admin);

			Role manager = new Role();
			manager.setName("MANAGER");
			mRoleService.save(manager);

			Role employee = new Role();
			employee.setName("EMPLOYEE");
			mRoleService.save(employee);

		}

		if (mEmployeeService.count() == 0) {
			Employee employee = new Employee();
			employee.setEmail(mEnvironment.getProperty("app.employee.admin.email"));
			employee.setPassword(mEnvironment.getProperty("app.employee.admin.password"));
			if (mRoleService.findById(1L).isPresent()) {
				employee.setRole(mRoleService.findById(1L).get());
			}else {
				throw new IllegalStateException();
			}
			mEmployeeService.save(employee);
		}

	}
}


package com.doozy.employees.web;

import com.doozy.employees.model.Department;
import com.doozy.employees.model.Employee;
import com.doozy.employees.model.Role;
import com.doozy.employees.service.DepartmentService;
import com.doozy.employees.service.EmployeeService;
import com.doozy.employees.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LoadDepartmentsFilter implements Filter {


	private static final String DEPARTMENTS = "departments";
	private static Logger logger = LoggerFactory.getLogger(LoadDepartmentsFilter.class);


	private final DepartmentService mDepartmentService;
	private final RoleService mRoleService;
	private final EmployeeService mEmployeeService;

	@Autowired
	public LoadDepartmentsFilter(DepartmentService departmentService, RoleService roleService, EmployeeService employeeService) {
		this.mDepartmentService = departmentService;
		mRoleService = roleService;
		mEmployeeService = employeeService;
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		logger.debug("Loading departments in filter from DB");
		Role admin = mRoleService.findById(1L).orElse(null);
		Role manager = mRoleService.findById(2L).orElse(null);
		Iterable<Department> departments;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			AtomicBoolean isAdmin = new AtomicBoolean(false);
			AtomicBoolean isManager = new AtomicBoolean(false);
			authentication.getAuthorities().forEach(role -> {
				if (role.getAuthority().equals(admin.name)) {
					isAdmin.set(true);
				} else if (role.getAuthority().equals(manager.name)) {
					isManager.set(true);
				}
			});
			if(isAdmin.get()) {
				departments = mDepartmentService.findAll();
			} else if (isManager.get()) {
				Employee employee = mEmployeeService.findByEmail(authentication.getName()).orElse(null);
				if (employee != null) {
					departments = Collections.singleton(mDepartmentService.findById(employee.getDepartment().getId()).orElse(null));
				} else {
					departments = Collections.emptyList();
				}
			} else {
				departments = Collections.emptyList();
			}
		} else {
			departments = Collections.emptyList();
		}
		httpRequest.setAttribute(DEPARTMENTS, departments);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}

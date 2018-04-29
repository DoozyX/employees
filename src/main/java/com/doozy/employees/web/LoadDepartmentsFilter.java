package com.doozy.employees.web;

import com.doozy.employees.model.Department;
import com.doozy.employees.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class LoadDepartmentsFilter implements Filter {


	private static final String DEPARTMENTS = "departments";
	private static Logger logger = LoggerFactory.getLogger(LoadDepartmentsFilter.class);


	private final DepartmentService mDepartmentService;

	@Autowired
	public LoadDepartmentsFilter(DepartmentService departmentService) {
		this.mDepartmentService = departmentService;
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		logger.debug("Loading categories in filter from DB");
		Iterable<Department> categories = mDepartmentService.findAll();
		httpRequest.setAttribute(DEPARTMENTS, categories);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}

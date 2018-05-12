package com.doozy.employees.web.controllers;

import com.doozy.employees.config.thymeleaf.Layout;
import com.doozy.employees.model.Department;
import com.doozy.employees.model.Employee;
import com.doozy.employees.service.DepartmentService;
import com.doozy.employees.service.EmployeeService;
import com.doozy.employees.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LandingController {

	private final EmployeeService mEmployeeService;
	private final DepartmentService mDepartmentService;
	private final RoleService mRoleService;

	private static final String PAGE_ORDER_BY_CRITERIA = "firstName";

	@Autowired
	public LandingController(EmployeeService employeeService, DepartmentService departmentService, RoleService roleService) {
		this.mEmployeeService = employeeService;
		this.mDepartmentService = departmentService;
		mRoleService = roleService;
	}

	@Layout("layouts/master")
	@GetMapping("/")
	public String index(Model model, @RequestParam(required = false, defaultValue = "") String query, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "12") Integer size) {
		Page<Employee> employees;
		if (query == null || query.isEmpty()) {
			employees = mEmployeeService.findAll(getPageable(page, size));
		} else {
			employees = mEmployeeService.findAll(getPageable(page, size));
			//employees = mEmployeeService.findByNameLike(getPageable(page,size), "%" + query + "%");
		}


		model.addAttribute("prevPageRequest", buildURIFromParams(query, employees.isFirst() ? 0 : employees.getNumber() - 1, size));
		model.addAttribute("nextPageRequest", buildURIFromParams(query, employees.isLast() ? employees.getNumber() : employees.getNumber() + 1, size));
		model.addAttribute("query", query);
		model.addAttribute("employees", employees.getContent());
		model.addAttribute("pageNumber", employees.getNumber());
		model.addAttribute("prevPage", employees.isFirst() ? 0 : employees.getNumber() - 1);
		model.addAttribute("nextPage", employees.isLast() ? employees.getNumber() : employees.getNumber() + 1);
		model.addAttribute("hasNext", employees.hasNext());
		model.addAttribute("hasPrev", employees.hasPrevious());
		return "fragments/contents";
	}

	@Layout("layouts/master")
	@GetMapping("/departments")
	public String categories() {
		return "fragments/department-list";
	}

	@Layout("layouts/master")
	@GetMapping("/department")
	public String department() {
		return "fragments/department-form";
	}

	@Layout("layouts/master")
	@GetMapping("/department/new")
	public String addDepartment() {
		return "fragments/department-form";
	}

	@Layout("layouts/master")
	@GetMapping("/department/{id}/edit")
	public String editDepartment(@PathVariable Long id, Model model) {
		String layout = "fragments/department-form";
		Optional<Department> department = mDepartmentService.findById(id);
		if (department.isPresent()) {
			model.addAttribute("department", department.get());
		} else {
			layout = "fragments/error";
		}
		return layout;
	}

	@Layout("layouts/master")
	@GetMapping("/department/{departmentId}")
	public String departmentFeed(@PathVariable Long departmentId, Model model, @RequestParam(required = false, defaultValue = "") String query, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "12") Integer size) {
		Page<Employee> employees;
		if (query == null || query.isEmpty()) {
			employees = mEmployeeService.findByDepartmentId(getPageable(page, size), departmentId);
		} else {
			employees = mEmployeeService.findByDepartmentId(getPageable(page, size), departmentId);
			//employees = mEmployeeService.findByDepartmentId(getPageable(page,size), "%" + query + "%");
		}


		model.addAttribute("prevPageRequest", buildURIFromParams(query, employees.isFirst() ? 0 : employees.getNumber() - 1, size));
		model.addAttribute("nextPageRequest", buildURIFromParams(query, employees.isLast() ? employees.getNumber() : employees.getNumber() + 1, size));
		model.addAttribute("query", query);
		model.addAttribute("employees", employees.getContent());
		model.addAttribute("pageNumber", employees.getNumber());
		model.addAttribute("prevPage", employees.isFirst() ? 0 : employees.getNumber() - 1);
		model.addAttribute("nextPage", employees.isLast() ? employees.getNumber() : employees.getNumber() + 1);
		model.addAttribute("hasNext", employees.hasNext());
		model.addAttribute("hasPrev", employees.hasPrevious());
		return "fragments/contents";
	}

	@Layout("layouts/master")
	@GetMapping("/employee/create")
	public String employee(Model model) {
		model.addAttribute("roles", mRoleService.findAll());
		return "fragments/employee-form";
	}

	@Layout("layouts/master")
	@GetMapping("/employee/{employeeId}")
	public String loadEmployee(@PathVariable Long employeeId, Model model) {
		String layout = "fragments/employee-details";
		Optional<Employee> employee = mEmployeeService.findById(employeeId);
		model.addAttribute("roles", mRoleService.findAll());

		if (employee.isPresent()) {
			model.addAttribute("employee", employee.get());
		} else {
			System.out.println("NO EMPLOYEE");
			layout = "fragments/error";
		}

		return layout;

	}

	@Layout("layouts/master")
	@GetMapping("/employee/{employeeId}/edit")
	public String editEmployee(@PathVariable Long employeeId, Model model) {
		String layout = "fragments/employee-form";
		Optional<Employee> employee = mEmployeeService.findById(employeeId);
		model.addAttribute("roles", mRoleService.findAll());

		if (employee.isPresent()) {
			model.addAttribute("employee", employee.get());
		} else {
			layout = "fragments/error";
		}

		return layout;

	}

	private Pageable getPageable(Integer page, Integer size) {
		return new PageRequest(page, size, new Sort(Sort.Direction.DESC, PAGE_ORDER_BY_CRITERIA));
	}

	private static String buildURIFromParams(String searchQuery, Integer page, Integer size) {
		return "/?query=" + searchQuery + "&page=" + page + "&size=" + size;
	}
}

package com.doozy.employees.web.controllers;


import com.doozy.employees.config.thymeleaf.Layout;
import com.doozy.employees.model.Employee;
import com.doozy.employees.service.DepartmentService;
import com.doozy.employees.service.EmployeeService;
import com.doozy.employees.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	private final EmployeeService mEmployeeService;
	private final DepartmentService mDepartmentService;
	private final RoleService mRoleService;

	@Autowired
	public EmployeeController(EmployeeService employeeService, DepartmentService departmentService, RoleService roleService) {
		mEmployeeService = employeeService;
		mDepartmentService = departmentService;
		mRoleService = roleService;
	}

	@Layout("layouts/master")
	@GetMapping("/create")
	public String employee(Model model) {
		model.addAttribute("roles", mRoleService.findAll());
		model.addAttribute("employee", new Employee());
		return "fragments/employee-form";
	}

	@Layout("layout/master")
	@PostMapping(value="/save")
	public String saveEmployee(@ModelAttribute Employee employee) {
		//model.addAttribute("categories",categoryService.findAll());
			System.out.println(employee.getId());
		mEmployeeService.save(employee);
		return "redirect:/employee/" + employee.id;
	}

	@Layout("layouts/master")
	@GetMapping("/{employeeId}")
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
	@GetMapping("/{employeeId}/edit")
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
}

package com.doozy.employees.web.controllers;


import com.doozy.employees.config.thymeleaf.Layout;
import com.doozy.employees.model.Employee;
import com.doozy.employees.service.EmployeeService;
import com.doozy.employees.service.RoleService;
import com.doozy.employees.web.dto.EmployeeDto;
import com.doozy.employees.web.dto.mappers.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	private final EmployeeService mEmployeeService;
	private final RoleService mRoleService;

	@Autowired
	public EmployeeController(EmployeeService employeeService, RoleService roleService) {
		mEmployeeService = employeeService;
		mRoleService = roleService;
	}

	@Layout("layouts/master")
	@GetMapping("/create")
	public String createEmployee(Model model) {
		model.addAttribute("roles", mRoleService.findAll());
		model.addAttribute("employee", new EmployeeDto());
		return "fragments/employee-form";
	}

	@Layout("layouts/master")
	@PostMapping("/create")
	public String insertEmployee(@ModelAttribute EmployeeDto employeeDto) {
		Employee employee = new Employee();
		EmployeeMapper.mapDtoToEmployee(employeeDto, employee);
		mEmployeeService.save(employee);
		return "redirect:/employee/" + employee.getId();
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
			model.addAttribute("employee", EmployeeMapper.mapEmployeeToDto(employee.get()));
		} else {
			layout = "fragments/error";
		}

		return layout;

	}

	@Layout("layouts/master")
	@PostMapping("/{employeeId}/edit")
	public String saveEmployee(@PathVariable Long employeeId, @ModelAttribute EmployeeDto employeeDto) {
		if (mEmployeeService.findById(employeeId).isPresent()) {
			Employee employee = mEmployeeService.findById(employeeId).get();
			employee = EmployeeMapper.mapDtoToEmployee(employeeDto, employee);
			mEmployeeService.save(employee);
			return "redirect:/employee/" + employeeId;
		}
		throw new IllegalStateException();
	}
}

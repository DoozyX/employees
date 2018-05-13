package com.doozy.employees.web.controllers;

import com.doozy.employees.config.thymeleaf.Layout;
import com.doozy.employees.model.Department;
import com.doozy.employees.model.Employee;
import com.doozy.employees.service.DepartmentService;
import com.doozy.employees.service.EmployeeService;
import com.doozy.employees.web.dto.DepartmentDto;
import com.doozy.employees.web.dto.mappers.DepartmentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/department")
public class DepartmentController {

	private final EmployeeService mEmployeeService;
	private final DepartmentService mDepartmentService;

	private static final String PAGE_ORDER_BY_CRITERIA = "firstName";

	public DepartmentController(EmployeeService employeeService, DepartmentService departmentService) {
		mEmployeeService = employeeService;
		mDepartmentService = departmentService;
	}


	@Layout("layouts/master")
	@GetMapping("/")
	public String categories() {
		return "fragments/department-list";
	}


	@Layout("layouts/master")
	@GetMapping("/create")
	public String addDepartment(Model model) {
		model.addAttribute("department", new DepartmentDto());
		return "fragments/department-form";
	}

	@Layout("layouts/master")
	@PostMapping("/create")
	public String insertDepartment(@ModelAttribute DepartmentDto departmentDto) {
		Department department = new Department();
		mDepartmentService.save(DepartmentMapper.mapDtoToDepartment(departmentDto, department));
		return "redirect:/department/" + department.id;
	}

	@Layout("layouts/master")
	@GetMapping("/{id}/edit")
	public String editDepartment(@PathVariable Long id, Model model) {
		String layout = "fragments/department-form";
		Optional<Department> department = mDepartmentService.findById(id);
		if (department.isPresent()) {
			model.addAttribute("department", DepartmentMapper.mapDepartmentToDto(department.get()));
		} else {
			layout = "fragments/error";
		}
		return layout;
	}

	@Layout("layouts/master")
	@PostMapping("/{id}/edit")
	public String saveDepartment(@PathVariable Long id, @ModelAttribute DepartmentDto departmentDto) {
		if (mDepartmentService.findById(id).isPresent()) {
			Department department = mDepartmentService.findById(id).get();
			DepartmentMapper.mapDtoToDepartment(departmentDto, department);
			mDepartmentService.save(department);
			return "redirect:/department/" + id;
		}
		throw new IllegalStateException();
	}

	@Layout("layouts/master")
	@GetMapping("/{departmentId}")
	public String departmentFeed(@PathVariable Long departmentId, Model
			model, @RequestParam(required = false, defaultValue = "") String
			                             query, @RequestParam(required = false, defaultValue = "0") Integer
			                             page, @RequestParam(required = false, defaultValue = "12") Integer size) {
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

	private Pageable getPageable(Integer page, Integer size) {
		return new PageRequest(page, size, new Sort(Sort.Direction.DESC, PAGE_ORDER_BY_CRITERIA));
	}

	private static String buildURIFromParams(String searchQuery, Integer page, Integer size) {
		return "/?query=" + searchQuery + "&page=" + page + "&size=" + size;
	}
}

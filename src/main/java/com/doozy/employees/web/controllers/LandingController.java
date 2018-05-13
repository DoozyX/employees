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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LandingController {

	private final EmployeeService mEmployeeService;

	private static final String PAGE_ORDER_BY_CRITERIA = "firstName";

	@Autowired
	public LandingController(EmployeeService employeeService) {
		this.mEmployeeService = employeeService;
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

	private Pageable getPageable(Integer page, Integer size) {
		return new PageRequest(page, size, new Sort(Sort.Direction.DESC, PAGE_ORDER_BY_CRITERIA));
	}

	private static String buildURIFromParams(String searchQuery, Integer page, Integer size) {
		return "/?query=" + searchQuery + "&page=" + page + "&size=" + size;
	}
}

package com.doozy.employees.web.controllers;

import com.doozy.employees.config.thymeleaf.Layout;
import com.doozy.employees.model.Department;
import com.doozy.employees.model.Employee;
import com.doozy.employees.service.DepartmentService;
import com.doozy.employees.service.EmployeeService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LandingController {

	private final EmployeeService employeeService;
	private final DepartmentService departmentService;

	private static final String PAGE_ORDER_BY_CRITERIA = "name";

	@Autowired
	public LandingController(EmployeeService employeeService, DepartmentService departmentService) {
		this.employeeService = employeeService;
		this.departmentService = departmentService;
	}

	@Layout("layouts/master")
	@GetMapping("/")
	public String index(Model model, @RequestParam(required = false, defaultValue = "") String query, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "1") Integer size) {
		Page<Department> departments;
		//if(query == null || query.isEmpty()) {
			departments = departmentService.findAll(getPageable(page, size));
		//}
		//else{
		//	departments = departmentService.findByName(getPageable(page,size), "%" + query + "%");
		//}
		model.addAttribute("prevPageRequest", buildURIFromParams(query, departments.isFirst() ? 0: departments.getNumber() - 1, size));
		model.addAttribute("nextPageRequest", buildURIFromParams(query, departments.isLast() ? departments.getNumber() : departments.getNumber() + 1, size));
		model.addAttribute("query", query);
		model.addAttribute("employees", departments.getContent());
		model.addAttribute("pageNumber", departments.getNumber());
		model.addAttribute("prevPage", departments.isFirst() ? 0: departments.getNumber() - 1);
		model.addAttribute("nextPage", departments.isLast() ? departments.getNumber() : departments.getNumber() + 1);
		model.addAttribute("hasNext", departments.hasNext());
		model.addAttribute("hasPrev", departments.hasPrevious());
		return "fragments/contents";
	}

    @GetMapping("/employee/{id}")
    public String employees(Model model, @PathVariable Long id) {
        Optional<Employee> optEmployee = employeeService.findById(id);

	    Employee noEmployee = new Employee();
	    noEmployee.setFirstName("No employee with id: " + id);

        model.addAttribute("employee", optEmployee.orElse(noEmployee));
        return "employee";
    }

	@Layout("layouts/master")
	@GetMapping("/categories")
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
		Optional<Department> department =  departmentService.findById(id);
		if(department.isPresent()){
			model.addAttribute("department", department.get());
		}
		else{
			layout = "fragments/error";
		}
		return layout;
	}

	@Layout("layouts/master")
	@GetMapping("/department/{departmentId}")
	public String departmentFeed(@PathVariable Long departmentId, Model model) {
		List<Employee> employeeList = new ArrayList<>();
		employeeService.findById(departmentId).ifPresent(employeeList::add);
		model.addAttribute("employees", employeeList);
		return "fragments/contents";
	}

	@Layout("layouts/master")
	@GetMapping("/employee/create")
	public String employee() {
		return "fragments/employee-form";
	}

	@Layout("layouts/master")
	@GetMapping("/employee/{employeeId}")
	public String loadEmployee(@PathVariable Long employeeId, Model model) {
		String layout = "fragments/employee-details";
		Optional<Employee> employee = employeeService.findById(employeeId);

		if(employee.isPresent()){
			model.addAttribute("employee", employee.get());
		}
		else{
			layout = "fragments/error";
		}

		return layout;

	}

	@Layout("layouts/master")
	@GetMapping("/employee/{employeeId}/edit")
	public String editEmployee(@PathVariable Long employeeId, Model model) {
		String layout = "fragments/employee-form";
		Optional<Employee> employee = employeeService.findById(employeeId);

		if(employee.isPresent()){
			model.addAttribute("employee", employee.get());
		}
		else{
			layout = "fragments/error";
		}

		return layout;

	}

	private Pageable getPageable(Integer page, Integer size){
		return new PageRequest(page, size, new Sort(Sort.Direction.DESC, PAGE_ORDER_BY_CRITERIA));
	}

	private static String buildURIFromParams(String searchQuery, Integer page, Integer size){
		return "/?query=" + searchQuery + "&page=" + page + "&size=" + size;
	}
}

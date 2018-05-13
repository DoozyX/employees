package com.doozy.employees.web.rest;

import com.doozy.employees.model.exceptions.EntityNotFoundException;
import com.doozy.employees.model.Employee;
import com.doozy.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/employee", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestEmployeeController {

	private final EmployeeService mEmployeeService;

	@Autowired
	public RestEmployeeController(EmployeeService mEmployeeService) {
		this.mEmployeeService = mEmployeeService;
	}

	//GET
	@GetMapping(value = {"/list", "/"})
	public List<Employee> getAllEmployees() {
		return new ArrayList<>(mEmployeeService.findAll());
	}

	@GetMapping(value = "/{id}")
	public Employee findById(@PathVariable(name = "id") Long id) {
		Optional<Employee> optEmployee = mEmployeeService.findById(id);

		return optEmployee
				.orElseThrow(() -> new EntityNotFoundException("No employee with id: " + id));
	}


	@GetMapping(value = "/byId")
	public Employee findByIdFromRequest(@RequestParam Long id) {
		Optional<Employee> optEmployee = mEmployeeService.findById(id);

		return optEmployee
				.orElseThrow(() -> new EntityNotFoundException("No employee with id: " + id));

	}

	//POST
	@PostMapping(value = {"/save"},
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Employee save(@RequestBody @Valid Employee employee) {
		return mEmployeeService.save(employee);
	}

	//PATCH
	@PatchMapping(value = {"/update"},
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Employee update(@RequestBody @Valid Employee employee) {
		Employee oldEmployee = mEmployeeService.findByEmail(employee.email).orElseThrow(() -> new EntityNotFoundException("No employee with id: " + employee.getId()));
		employee.setPassword(oldEmployee.getPassword());
		return mEmployeeService.save(employee);
	}

	//DELETE
	@DeleteMapping(value = "/{id}")
	public Employee deleteById(@PathVariable(name = "id") Long id) {
		Employee employee = mEmployeeService.findById(id).orElseThrow(() -> new EntityNotFoundException("No employee with id: " + id));
		mEmployeeService.delete(employee);
		return employee;
	}

}
package com.doozy.employees.web.rest;

import com.doozy.employees.model.exceptions.EntityNotFoundException;
import com.doozy.employees.model.Department;
import com.doozy.employees.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/department", produces = MediaType.APPLICATION_JSON_VALUE)
public class DepartmentController {

	private final DepartmentService mDepartmentService;

	@Autowired
	public DepartmentController(DepartmentService departmentService) {
		this.mDepartmentService = departmentService;
	}

	//GET
	@GetMapping(value = {"/list", "/"})
	public List<Department> getAllEmployees() {
		return new ArrayList<>(mDepartmentService.findAll());
	}

	@GetMapping(value = "/{id}")
	public Department findById(@PathVariable(name = "id") Long id) {
		Optional<Department> optDepartment = mDepartmentService.findById(id);

		return optDepartment
				.orElseThrow(() -> new EntityNotFoundException("No department with id: " + id));
	}


	@GetMapping(value = "/byId")
	public Department findByIdFromRequest(@RequestParam Long id) {
		Optional<Department> optDepartment = mDepartmentService.findById(id);

		return optDepartment
				.orElseThrow(() -> new EntityNotFoundException("No department with id: " + id));

	}

	//POST
	@PostMapping(value = {"/save"},
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Department save(@RequestBody @Valid Department department) {
		mDepartmentService.save(department);
		return department;
	}

	//PATCH
	@PatchMapping(value = {"/update"},
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Department update(@RequestBody @Valid Department department) {
		mDepartmentService.save(department);
		return department;
	}

	//DELETE
	@DeleteMapping(value = "/{id}")
	public Department deleteById(@PathVariable(name = "id") Long id) {
		Department department = mDepartmentService.findById(id).orElseThrow(() -> new EntityNotFoundException("No department with id: " + id));

		mDepartmentService.delete(department);
		return department;
	}

}
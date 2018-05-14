package com.doozy.employees.tasks;

import java.util.Date;

import com.doozy.employees.model.Employee;
import com.doozy.employees.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RemoveInactiveEmployees {

	private static final Logger log = LoggerFactory.getLogger(RemoveInactiveEmployees.class);

	private final EmployeeService mEmployeeService;

	@Autowired
	public RemoveInactiveEmployees(EmployeeService employeeService) {
		mEmployeeService = employeeService;
	}

	@Scheduled(fixedRate = 3600000)
	public void reportCurrentTime() {
		Iterable<Employee> employees =  mEmployeeService.findByActivatedFalse();
		employees.forEach(employee -> {
			int diffInDays = (int)( (new Date().getTime() - new Date(employee.getRegistration()).getTime())
					/ (1000 * 60 * 60 * 24) );
			if (diffInDays >= 1) {
				mEmployeeService.delete(employee);
			}

		});
	}
}

package com.doozy.employees.events;

import com.doozy.employees.model.Employee;
import org.springframework.context.ApplicationEvent;

public class EmployeeRegisteredEvent extends ApplicationEvent {
	public EmployeeRegisteredEvent(Employee employee) {
		super(employee);
	}

	public Employee getEmployee() {
		return (Employee) getSource();
	}

}

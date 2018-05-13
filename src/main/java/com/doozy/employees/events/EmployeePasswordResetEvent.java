package com.doozy.employees.events;

import com.doozy.employees.model.Employee;
import org.springframework.context.ApplicationEvent;

public class EmployeePasswordResetEvent  extends ApplicationEvent {
	public EmployeePasswordResetEvent(Employee employee) {
		super(employee);
	}

	public Employee getEmployee() {
		return (Employee) getSource();
	}
}

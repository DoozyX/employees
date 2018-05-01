package com.doozy.employees.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Indexed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Indexed
@Entity
public class Employee {
	enum Gender {
		MALE,
		FEMALE
	}

	public enum Role {
		EMPLOYEE,
		MANAGER,
		ADMIN
	}

	@Id
	@GeneratedValue
	public Long id;

	public String username;

	public String email;
	public String password;
	public String firstName;
	public String lastName;
	public Gender gender;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Department department;

	public LocalDate birthDate;
	public boolean activated;
	public LocalDateTime registration;
	public String activationCode;
	public Role role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public LocalDateTime getRegistration() {
		return registration;
	}

	public void setRegistration(LocalDateTime registration) {
		this.registration = registration;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public static String[] getRoles() {
		return Stream.of(Role.values()).map(Role::name).toArray(String[]::new);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Employee)) return false;
		Employee employee = (Employee) o;
		return Objects.equals(getId(), employee.getId());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getId());
	}
}

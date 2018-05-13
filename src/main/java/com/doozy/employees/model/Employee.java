package com.doozy.employees.model;

import com.doozy.employees.web.dto.RegisterEmployeeDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Indexed
@Entity
@Getter
@Setter
public class Employee implements UserDetails {
	public Employee() {
	}

	/**
	 * Load employee basic properties from dto
	 * Set password after
	 * TODO: fix date missing
	 * @param registerEmployeeDto
	 */
	public Employee(RegisterEmployeeDto registerEmployeeDto) {
		email = registerEmployeeDto.email;
		firstName = registerEmployeeDto.firstName;
		lastName = registerEmployeeDto.lastName;
		gender = registerEmployeeDto.gender;
	}

	public enum Gender {
		MALE("Male"),
		FEMALE("Female");

		private final String displayName;

		Gender(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

	@Id
	@GeneratedValue
	public Long id;

	public String email;
	public String password;
	public String firstName;
	public String lastName;
	public Gender gender;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Department department;

	public String birthDate;
	public boolean activated;
	public String registration;
	public String activationCode;

	@ManyToOne
	public Role role;

	public Provider provider;

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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grants = new ArrayList<>();
		grants.add(role);
		return grants;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
			return false;
	}

	@Override
	public boolean isEnabled() {
		return activated;
	}
}

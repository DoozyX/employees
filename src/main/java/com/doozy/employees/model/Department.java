package com.doozy.employees.model;


import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.Objects;

@Indexed
@Entity
//@Table(name = "departments")
//@Where(clause = "deleted=false")
@Cacheable
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Department)) return false;
		Department that = (Department) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}

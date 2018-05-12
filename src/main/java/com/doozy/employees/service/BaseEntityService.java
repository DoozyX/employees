package com.doozy.employees.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;

public interface BaseEntityService<T> {

	public Optional<T> findById(Long id);

	public Collection<T> findAll();

	Page<T> findAll(Pageable pageable);

	public T save(T entity);

	void delete(T entity);

	Long count();
}
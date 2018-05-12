package com.doozy.employees.persistance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;

public interface BaseRepository<T,S> {

	@Deprecated
	Collection<T> findAll();

	Page<T> findAll(Pageable pageable);

	Optional<T> findById(S id);

	T save(T entity);

	void delete(T entity);

	Long count();
}
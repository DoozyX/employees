package com.doozy.employees.persistance.jpa;


import com.doozy.employees.persistance.VerificationTokenRepository;
import com.doozy.employees.web.dto.EmployeeVerificationToken;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface JpaVerificationTokenRepository extends VerificationTokenRepository, JpaRepository<EmployeeVerificationToken, Long> {

}

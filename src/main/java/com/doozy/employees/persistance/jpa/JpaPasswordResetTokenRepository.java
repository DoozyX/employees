package com.doozy.employees.persistance.jpa;


import com.doozy.employees.persistance.PasswordResetTokenRepository;
import com.doozy.employees.model.PasswordResetToken;
import org.springframework.data.repository.Repository;

public interface JpaPasswordResetTokenRepository extends PasswordResetTokenRepository, Repository<PasswordResetToken, Long> {
}

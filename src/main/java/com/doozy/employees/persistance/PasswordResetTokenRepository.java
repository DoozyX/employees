package com.doozy.employees.persistance;


import com.doozy.employees.model.PasswordResetToken;

public interface PasswordResetTokenRepository {

  void save(PasswordResetToken token);

  PasswordResetToken findByToken(String token);

  void delete(PasswordResetToken passwordResetToken);
}

package com.doozy.employees.persistance;


import com.doozy.employees.web.dto.EmployeeVerificationToken;

import java.util.Date;

public interface VerificationTokenRepository {

  EmployeeVerificationToken save(EmployeeVerificationToken token);

  EmployeeVerificationToken findByToken(String token);

  EmployeeVerificationToken findByUser(String user);

  void deleteByExpiryDateLessThan(Date date);

  void delete(EmployeeVerificationToken token);
}

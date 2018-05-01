package com.doozy.employees.web.validators;


import com.doozy.employees.web.dto.EmployeeDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context){
    EmployeeDto user = (EmployeeDto) obj;
    return user.getPassword().equals(user.getMatchingPassword());
  }
}

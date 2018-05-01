package com.doozy.employees.model.exceptions;

public class DuplicateEmailException extends RuntimeException {

  public DuplicateEmailException() {
    super("Email already exists");
  }

}

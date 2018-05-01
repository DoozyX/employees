package com.doozy.employees.model.exceptions;

public class EmailNotFoundException extends RuntimeException {
  public EmailNotFoundException() {
    super("Email not found");
  }
}

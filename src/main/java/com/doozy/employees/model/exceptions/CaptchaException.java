package com.doozy.employees.model.exceptions;

public class CaptchaException extends RuntimeException {
  public CaptchaException(String message){
    super(message);
  }
}

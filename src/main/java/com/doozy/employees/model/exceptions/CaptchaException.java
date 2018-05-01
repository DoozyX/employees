package com.doozy.employees.model.exceptions;final

public class CaptchaException extends RuntimeException {
  public CaptchaException(String message){
    super(message);
  }
}

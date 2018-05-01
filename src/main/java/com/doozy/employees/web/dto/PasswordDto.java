package com.doozy.employees.web.dto;

import com.doozy.employees.web.validators.PasswordMatches;
import lombok.Getter;
import lombok.Setter;

@PasswordMatches
@Getter @Setter
public class PasswordDto {

  public String password;

  public String matchingPassword;

}

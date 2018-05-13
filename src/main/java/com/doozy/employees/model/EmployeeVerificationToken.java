package com.doozy.employees.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter @Setter
public class EmployeeVerificationToken {

  // Token expires every two hours
  private static final int EXPIRATION = 60 * 2;

  public EmployeeVerificationToken(){
    super();
    expiryDate = calculateExpiryDate();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String token;

  @OneToOne(targetEntity = Employee.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private Employee user;

  private Date expiryDate;

  private Date calculateExpiryDate() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Timestamp(cal.getTime().getTime()));
    cal.add(Calendar.MINUTE, EmployeeVerificationToken.EXPIRATION);
    return new Date(cal.getTime().getTime());
  }

}

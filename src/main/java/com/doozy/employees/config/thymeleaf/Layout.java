package com.doozy.employees.config.thymeleaf;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Layout {
    String NONE = "none";

    String value();
}

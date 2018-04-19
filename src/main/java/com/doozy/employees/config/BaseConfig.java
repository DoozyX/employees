package com.doozy.employees.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@ServletComponentScan
@EnableWebMvc
@EnableCaching(mode = AdviceMode.ASPECTJ)
public class BaseConfig {
}

package com.doozy.employees.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


//@ServletComponentScan
//@EnableWebMvc
@EnableAsync
@EnableScheduling
//@EnableCaching(mode = AdviceMode.ASPECTJ)
@Configuration
public class BaseConfig {
}

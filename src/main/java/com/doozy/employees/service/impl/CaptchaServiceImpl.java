package com.doozy.employees.service.impl;

import com.doozy.employees.config.captcha.CaptchaSettings;
import com.doozy.employees.config.captcha.GoogleResponse;
import com.doozy.employees.model.exceptions.CaptchaException;
import com.doozy.employees.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.regex.Pattern;

@Service("captchaService")
public class CaptchaServiceImpl implements CaptchaService {

  private final CaptchaSettings captchaSettings;

  private final RestOperations restTemplate;

  private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

	@Autowired
	public CaptchaServiceImpl(CaptchaSettings captchaSettings, @Lazy RestOperations restTemplate) {
		this.captchaSettings = captchaSettings;
		this.restTemplate = restTemplate;
	}

	@Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Override
  public void processResponse(String response, String clientIP) {
    if(!responseSanityCheck(response)) {
      throw new CaptchaException("Response contains invalid characters");
    }

    URI verifyUri = URI.create(String.format(
      "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
      getReCaptchaSecret(), response, clientIP));

    GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

    if (googleResponse == null || !googleResponse.isSuccess()) {
      throw new CaptchaException("reCaptcha was not successfully validated");
    }
  }

  private boolean responseSanityCheck(String response) {
    return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
  }


  @Override
  public String getReCaptchaSite() {
    return captchaSettings.getSite();
  }

  @Override
  public String getReCaptchaSecret() {
    return captchaSettings.getSecret();
  }
}

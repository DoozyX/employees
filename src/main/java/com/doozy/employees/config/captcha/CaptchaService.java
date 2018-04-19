package com.doozy.employees.config.captcha;


import com.doozy.employees.model.exceptions.CaptchaException;

public interface CaptchaService {

  void processResponse(final String response, final String clientIP) throws CaptchaException;

  String getReCaptchaSite();

  String getReCaptchaSecret();

}

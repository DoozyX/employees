package com.doozy.employees.service;

public interface MailService {
	void sendEmail(String email, String subject, String content);
}

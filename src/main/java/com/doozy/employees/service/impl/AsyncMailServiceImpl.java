package com.doozy.employees.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Primary
public class AsyncMailServiceImpl extends MailServiceImpl {

	@Autowired
	public AsyncMailServiceImpl(JavaMailSender mailSender) {
		super(mailSender);
	}

	@Async
	@Override
	public void sendEmail(String email, String subject, String content) {
		super.sendEmail(email, subject, content);
	}
}
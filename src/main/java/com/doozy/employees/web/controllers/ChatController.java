package com.doozy.employees.web.controllers;

import com.doozy.employees.config.thymeleaf.Layout;
import com.doozy.employees.config.websocket.WebSocketConfig;
import com.doozy.employees.service.MessageSendingService;
import com.doozy.employees.web.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Riste Stojanov
 */
@Controller
public class ChatController {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");

	private final MessageSendingService messageSendingService;

	@Autowired
	public ChatController(MessageSendingService messageSendingService) {
		this.messageSendingService = messageSendingService;
	}

	@PostMapping(value = "/send",
			produces = "application/json")
	@ResponseBody
	public void send(@RequestParam String user, @RequestParam String message) {

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.user = user;
		chatMessage.message = message;
		chatMessage.time = formatter.format(LocalDateTime.now());
		messageSendingService.send(WebSocketConfig.DEFAULT_TOPIC, chatMessage);
	}

	@Layout("layouts/master")
	@GetMapping(value = "/chat")
	public String chat() {
		return "fragments/chat";
	}
}

package com.doozy.employees.service.impl;

import com.doozy.employees.config.websocket.WebSocketConfig;
import com.doozy.employees.service.MessageSendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

@Service
public class WebSocketMessageSendingServiceImpl implements MessageSendingService {


	private final SimpMessagingTemplate template;

	@Autowired
	public WebSocketMessageSendingServiceImpl(SimpMessagingTemplate template) {
		this.template = template;
	}

	@Override
	public void send(String topic, Object data, String... topicParams) {
		if (!topic.startsWith(WebSocketConfig.TOPIC_PREFIX)) {
			topic = WebSocketConfig.TOPIC_PREFIX + topic;
		}
		String uri = new UriTemplate(topic).expand((Object[]) topicParams).toString();
		template.convertAndSend(uri, data);
	}

	@Override
	public void sendToUser(String user, String topic, Object data, String... topicParams) {
		if (!topic.startsWith(WebSocketConfig.TOPIC_PREFIX)) {
			topic = WebSocketConfig.TOPIC_PREFIX + topic;
		}
		String uri = new UriTemplate(topic).expand((Object[]) topicParams).toString();
		template.convertAndSendToUser(user, uri, data);
	}
}
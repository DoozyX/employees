package com.doozy.employees.service;

public interface MessageSendingService {

	void send(String topic, Object data, String... topicParams);

	void sendToUser(String user, String topic, Object data, String... topicParams);
}

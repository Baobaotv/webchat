package com.websocket.service;

import java.util.List;

import com.websocket.dto.MessageDto;
import com.websocket.entity.MessageEntity;

public interface MessageService {
	List<MessageEntity> findBySenderOrReceiver(Long sender, Long receiver);
	MessageEntity save(MessageDto dto);
}

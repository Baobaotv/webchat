package com.websocket.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.websocket.dto.MessageDto;
import com.websocket.entity.MessageEntity;
import com.websocket.entity.UserEntity;
import com.websocket.repository.MessageRepository;
@Service
public class MessageServiceImpl implements MessageService{
	
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private UserService userServiceImpl;

	@Override
	public List<MessageEntity> findBySenderOrReceiver(Long sender, Long receiver) {
		// TODO Auto-generated method stub
		return messageRepository.findBySenderIdOrReceiverIdOrderByCreatedDateAsc( sender,  receiver);
	}

	@Override
	public MessageEntity save(MessageDto dto) {
		MessageEntity entity = new MessageEntity();
		entity.setContent(dto.getContent());
		UserEntity sender=userServiceImpl.findOneById(dto.getSenderId());
		UserEntity receiver= userServiceImpl.findOneById(dto.getReceiverId());
		entity.setSender(sender);
		entity.setReceiver(receiver);
		entity.setCreatedDate(new Date());
		entity= messageRepository.save(entity);
		return entity;
	}

}

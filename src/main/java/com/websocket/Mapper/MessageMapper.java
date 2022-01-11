package com.websocket.Mapper;

import com.websocket.dto.MessageDto;
import com.websocket.entity.MessageEntity;

public class MessageMapper {
	public static MessageDto convertToDto(MessageEntity entity) {
		MessageDto dto= new MessageDto();
		dto.setId(entity.getId());
		dto.setContent(entity.getContent());
		dto.setSenderId(entity.getSender().getId());
		dto.setReceiverId(entity.getReceiver().getId());
		if(entity.getCreatedDate()!=null) {
			dto.setCreatedDate(entity.getCreatedDate().toString());
		}
		
		return dto;
	}
	

}

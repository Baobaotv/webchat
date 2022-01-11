package com.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.websocket.Mapper.MessageMapper;
import com.websocket.dto.MessageDto;
import com.websocket.entity.MessageEntity;
import com.websocket.model.ChatMessage;
import com.websocket.service.MessageService;
import com.websocket.service.UserService;

@Controller
public class WebSocketController {
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private MessageService messageServiceImpl;
	
	@Autowired 
	private UserService userServiceImpl;
//Server send
    @MessageMapping("/sendToUSer")
//    @SendTo("/topic/publicChatRoom")
    public void sendMessage(@Payload MessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    
    	String username=chatMessage.getReceiver();
    	MessageEntity entity= messageServiceImpl.save(chatMessage);
    		simpMessagingTemplate.convertAndSend("/topic/"+username, chatMessage);
//        return chatMessage;
    }
    
//    UserSend
    @MessageMapping("/public")
//  @SendTo("/topic/publicChatRoom")
  public void userSendMessage(@Payload MessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    	MessageEntity entity= messageServiceImpl.save(chatMessage);
    	MessageDto dto = MessageMapper.convertToDto(entity);
  		simpMessagingTemplate.convertAndSend("/topic/public", dto);
//      return chatMessage;
  }

    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/publicChatRoom")
    public void addUser(@Payload MessageDto messageDto, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    	userServiceImpl.updatePeerId( messageDto.getSenderId(), messageDto.getPeerId());
    	
   
    }
	@MessageMapping("/chat.sendMessage")
    @SendTo("/topic/publicChatRoom")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/publicChatRoom")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }

}

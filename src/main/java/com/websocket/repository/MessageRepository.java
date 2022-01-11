package com.websocket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.websocket.entity.MessageEntity;
import com.websocket.entity.UserEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long>{
	
	List<MessageEntity> findBySenderIdOrReceiverIdOrderByCreatedDateAsc(Long sender, Long receiver);
}

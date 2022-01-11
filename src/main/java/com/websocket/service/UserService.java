package com.websocket.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.websocket.entity.UserEntity;

public interface UserService {
	UserEntity findOneByUserName(String userName);
	UserEntity save(UserEntity entity);
	List<UserEntity> findAll();
	UserEntity findOneById(Long id);
	List<UserEntity> findLstUser();
	void  updatePeerId( Long id, String peerId);
}

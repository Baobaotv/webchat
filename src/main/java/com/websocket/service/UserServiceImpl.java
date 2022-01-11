package com.websocket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.websocket.entity.UserEntity;
import com.websocket.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserEntity findOneByUserName(String userName) {
		// TODO Auto-generated method stub
		return userRepository.findOneByUsername(userName);
	}

	@Override
	public UserEntity save(UserEntity entity) {
		// TODO Auto-generated method stub
		return userRepository.save(entity);
	}

	@Override
	public List<UserEntity> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public UserEntity findOneById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findOneById(id);
	}

	@Override
	public List<UserEntity> findLstUser() {
		// TODO Auto-generated method stub
		return userRepository.findLstUser();
	}

	@Override
	public void updatePeerId(Long id, String peerId) {
		try {
			String userId= Long.toString(id);
			userRepository.updatePeerId(peerId, userId);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
				
		
		
	}

	

}

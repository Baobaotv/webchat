package com.websocket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.websocket.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
	UserEntity save(UserEntity entity);
	UserEntity findOneByUsername(String userName);
	List<UserEntity> findAll();
	UserEntity findOneById(Long id);
	@Query(value = "select  DISTINCT sender_id , username, u.id, peer  from  websocket.user as u inner join websocket.message as m on u.id= m.sender_id  ORDER BY created_date  desc",nativeQuery = true)
	List<UserEntity> findLstUser();
	
	@Transactional
	 @Modifying
	@Query(value = "update websocket.user as u set u.peer = :peerId where u.id = :id",nativeQuery = true)
	void  updatePeerId(@Param("peerId") String peerId,@Param("id") String id);
}

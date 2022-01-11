package com.websocket.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "peer")
	private String peer;
	
	
	
//	@OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
//	private List<MessageEntity> lstMessageReciver= new ArrayList<MessageEntity>();
//	
//	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
//	private List<MessageEntity> lstMessageSender= new ArrayList<MessageEntity>();
	
	
	

//	public List<MessageEntity> getLstMessageReciver() {
//		return lstMessageReciver;
//	}
//
//	public void setLstMessageReciver(List<MessageEntity> lstMessageReciver) {
//		this.lstMessageReciver = lstMessageReciver;
//	}
//
//	public List<MessageEntity> getLstMessageSender() {
//		return lstMessageSender;
//	}
//
//	public void setLstMessageSender(List<MessageEntity> lstMessageSender) {
//		this.lstMessageSender = lstMessageSender;
//	}

	public String getPeer() {
		return peer;
	}

	public void setPeerId(String peer) {
		this.peer = peer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
	
	

}

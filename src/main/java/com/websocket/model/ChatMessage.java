package com.websocket.model;

public class ChatMessage {

	 private MessageType type;
	    private String content;
	    private String sender;
	    private String receiver;
	    private String senderId;
	    
	    

	    public String getSenderId() {
			return senderId;
		}

		public void setSenderId(String senderId) {
			this.senderId = senderId;
		}

		public String getReceiver() {
			return receiver;
		}

		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}

		public enum MessageType {
	        CHAT, JOIN, LEAVE
	    }

	    public MessageType getType() {
	        return type;
	    }

	    public void setType(MessageType type) {
	        this.type = type;
	    }

	    public String getContent() {
	        return content;
	    }

	    public void setContent(String content) {
	        this.content = content;
	    }

	    public String getSender() {
	        return sender;
	    }

	    public void setSender(String sender) {
	        this.sender = sender;
	    }
}

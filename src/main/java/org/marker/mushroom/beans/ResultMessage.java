package org.marker.mushroom.beans;


/**
 * 登录消息
 * @author marker
 * */
public class ResultMessage {

	//状态
	private boolean status;
	//消息
	private String message;
	
	
	/**
	 * 登录消息
	 * @param boolean status
	 * @param String message
	 * */
	public ResultMessage(boolean status, String message){
		this.status = status;
		this.message = message;
	}
	
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
}

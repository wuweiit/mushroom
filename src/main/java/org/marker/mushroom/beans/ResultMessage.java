package org.marker.mushroom.beans;


import java.io.Serializable;

/**
 * 响应消息
 *
 *
 * @author marker
 * */
public class ResultMessage implements Serializable {

	/** 状态 */
	private boolean status;


	/** 消息 */
	private String message;
	
	
	/**
	 * 登录消息
	 * @param  status 状态
	 * @param  message 消息内容
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

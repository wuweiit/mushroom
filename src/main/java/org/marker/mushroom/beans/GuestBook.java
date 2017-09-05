package org.marker.mushroom.beans;

import java.io.Serializable;
import java.util.Date;

import org.marker.mushroom.dao.annotation.Entity;

@Entity("guestbook")
public class GuestBook implements Serializable { 
	private static final long serialVersionUID = -4292951929182132458L;

	private int id;
	private String content;
	private String ip;
	private String nickname;
	private Date time;
	private int status;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}

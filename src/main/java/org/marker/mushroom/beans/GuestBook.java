package org.marker.mushroom.beans;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.marker.mushroom.dao.annotation.Entity;

@Data
@Entity("guestbook")
public class GuestBook implements Serializable { 
	private static final long serialVersionUID = -4292951929182132458L;

	private int id;
	private String content;
	private String ip;
	private String nickname;
	private Date time;
	private int status;
	

	
}

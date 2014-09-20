package org.marker.mushroom.beans;

import java.io.File;
import java.io.IOException;

import org.marker.mushroom.utils.FileTools;
import org.marker.security.Base64;
import org.marker.security.DES;



/**
 * 创始人对象
 * */
public class AdminControll {

	private String username;
	private String password;
	private String createtime;
 
	
	public void read(String path){
		try {
			String admin = FileTools.getFileContet(new File(path), FileTools.FILE_CHARACTER_UTF8);
			String s[] = admin.split("[|]");
			this.username   = s[0];
			this.password   = s[1];
			this.createtime = s[2];
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}




	public void write(String filePath, String key) throws Exception {

		// 加密字符串
		byte[] b = DES.encrypt(this.password.trim().getBytes(), key);
		String admin = this.username + "|" + Base64.encode(b) + "|"
				+ this.createtime;

		FileTools.setFileContet(new File(filePath), admin, FileTools.FILE_CHARACTER_UTF8);

	}
	
	
	
	
	
	
}

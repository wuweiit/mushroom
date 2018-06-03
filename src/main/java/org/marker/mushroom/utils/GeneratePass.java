package org.marker.mushroom.utils;

import org.marker.security.Base64;
import org.marker.security.DES;
import org.marker.security.MD5;

/**
 * 密码加密生成器
 * 
 * @author marker
 * */
public class GeneratePass {

	
	public static String encode(String password) {
		String key = "iSDCRbyRZP0=";
		try {
			return MD5.getMD5Code(Base64.encode(DES.encrypt(
                       password.getBytes(), key)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}

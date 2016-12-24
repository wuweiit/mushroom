package org.marker.mushroom.utils;

import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.security.Base64;
import org.marker.security.DES;
import org.marker.security.MD5;

/**
 * 密码加密生成器
 * 
 * @author marker
 * */
public class GeneratePass {

	private static final SystemConfig syscfg = SystemConfig.getInstance();
	
	public static String encode(String password) {
		String key = syscfg.get("secret_key");
		try {
			return MD5.getMD5Code(Base64.encode(DES.encrypt(
                       password.getBytes(), key)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}

package org.marker.security;

import org.apache.commons.codec.digest.DigestUtils;   

/**
 * MD5加密(依赖apache的DigestUtils类)
 * @author marker
 * */
public class MD5 {
	
	/**
	 * 私有构造方法
	 * */
    private MD5(){ }   
    
	
    /**
     * 获取MD5加密值
     * @return String md5值
     * 
     * */
	public static String getMD5Code(String s) {   
	        if (s == null) return null;
	        return DigestUtils.md5Hex(s);
	}   
  
    
	
	public static void main(String[] args){   
         String passwd =  "123撒倒萨";   
	     System.out.println(passwd + " 加密后为： " + MD5.getMD5Code(passwd));  
    }   
        
 }  
 

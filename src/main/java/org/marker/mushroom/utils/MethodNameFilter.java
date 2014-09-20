package org.marker.mushroom.utils;

import java.util.regex.Pattern;


/**
 * 方法名称过滤器（安全机制）
 * 这里设置类几个常过滤的方法名称
 * @author marker
 * 
 * */
public class MethodNameFilter {

	
	private static final Pattern urlPattern = Pattern.compile("(hashCode|getClass|equals|toString|notify|notifyAll|wait|set\\w+)?"); 
	
	
	/**
	 * 检测方法名称是否在过滤范围
	 * @param methodName 方法名称
	 * @return boolean 
	 * */
	public static boolean checkMethodName(String methodName){
		if(methodName == null) methodName = "";//验证
		return !urlPattern.matcher(methodName).matches();
	}
	
	
}

package org.marker.mushroom.utils;

import org.marker.mushroom.context.ActionContext;
import org.springframework.util.StringUtils;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * 
 * 不修改此类中的成员变量，则线程安全
 * 
 * 
 * 工具类
 * @author marker
 *
 */
public class HttpUtils {

	
	// 代理服务器的HTTP请求协议，客户端真实IP字段名称
	public static final String X_REAL_IP = "X-Real-IP";
	
	
	/**
	 * 获取请求对象的URL地址
	 * @param request 请求对象
	 * @return String URL地址（http://www.yl-blog.com）
	 */
	public static String getRequestURL(HttpServletRequest request) {
		StringBuilder url  = new StringBuilder();
		String scheme      = request.getScheme();
		String contextPath = request.getContextPath();
		int port           = request.getServerPort();
		url.append(scheme); // http, https
		url.append("://");
		url.append(request.getServerName());
		if (("http".equals(scheme) && port != 80)
				|| ("https".equals(scheme) && port != 443)) {
			url.append(':');
			url.append(request.getServerPort());
		}
		url.append(contextPath);
		return url.toString();
	}
	
	
	
	/**
	 * for nigx返向代理构造 获取客户端IP地址
	 * @param request
	 * @return
	 */
	public static String getRemoteHost(HttpServletRequest request){
		String ip = request.getHeader(X_REAL_IP);// 获取代理IP地址
		return (null == ip)? request.getRemoteHost() : ip; 
	}
	
	/**
	 * 检查是否有cookieName
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static boolean checkCookie(HttpServletRequest request, String cookieName){
		Cookie cookie;  
		Cookie[] cookies = request.getCookies();
	    if( cookies != null )
	    	for (int i = 0; i < cookies.length; i++){
	    		cookie = cookies[i];
	    		if(cookie.getName().equals(cookieName))
	    			return true;
	    	} 
		return false;
	}
	
	
	public static String getCookie(HttpServletRequest request, String cookieName){
		Cookie cookie;  
		Cookie[] cookies = request.getCookies();
	    if( cookies != null )
	    	for (int i = 0; i < cookies.length; i++){
	    		cookie = cookies[i];
	    		if(cookie.getName().equals(cookieName))
	    			return cookie.getValue();
	    	} 
		return "";
	}
	
	
	
	/**
	 * 获取语言信息
	 * 
	 * 1. 优先获取URL重写参数
	 * 2. 获取Http请求浏览器语言标识
	 * 
	 * @param request
	 * @return
	 */
	public static String getLanguage(HttpServletRequest request){
		String lang = request.getParameter("lang");
		if(lang != null && !lang.equals("")){
            return lang;
		}else{
            // 2016-10-22 marker 获取cookies中的语言
			lang = getCookie(request,"lang");
			if(!StringUtils.isEmpty(lang)){
				return lang;
			}
		}
		Locale locale = request.getLocale();
		return locale.getLanguage()+"-"+locale.getCountry();
	}
}

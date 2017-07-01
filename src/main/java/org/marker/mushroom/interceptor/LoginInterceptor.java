package org.marker.mushroom.interceptor;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;



/**
 * 判断是否登录，如果没有登录就重定向到/admin/login.do
 * 
 * 拦截路径：/admin/
 * 
 * @author marker
 * 
 * */
public class LoginInterceptor implements HandlerInterceptor  {



	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute(AppStatic.WEB_APP_SESSSION_LOGINNAME);
		if(username != null){
			return true;
		}else{
			PrintWriter out = response.getWriter();
			String accept = request.getHeader("accept"); 
			
			if(accept.matches(".*application/json.*")){// json数据请求 
				out.write("{\"status\":false,\"code\":\"101\",\"message\":\"当前会话失效，请重新登录系统!\"}");
			}
			
			// HTML页面
			out.write("<script type='text/javascript'>window.location.href='login.do?status=timeout';</script>");
			out.flush();
			out.close();
			return false;
		} 
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}

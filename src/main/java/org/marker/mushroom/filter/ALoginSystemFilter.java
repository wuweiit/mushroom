package org.marker.mushroom.filter;

import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.utils.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * [管理后台登录过滤器]
 * 判断是否登录，如果没有登录就重定向到/admin/login.do
 * 支持admin路径、plugin路径
 *  
 * @author marker
 * */
@WebFilter(
		filterName = "11_LoginSystemFilter",
		urlPatterns ={ "/admin/*","/plugin/*" })
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class ALoginSystemFilter implements Filter {

	/** 日志记录器 */ 
	protected Logger logger =  LoggerFactory.getLogger(ALoginSystemFilter.class);

	/** 排除不需要登录的URL */
	public static String[] excludeUrls = new String[]{
		"/admin/login.do",
		"/admin/loginSystem.do"
	};
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest  request  = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String uri = request.getRequestURI();
		if (!uri.endsWith(".do")) {
			chain.doFilter(request, response);
			return;
		}
		if (ArrayUtils.containsStartWith(excludeUrls, uri)) {
			chain.doFilter(request, response);
			return;
		}
		// 判断不是/plugin/*.do 则直接绕过登录验证
		if (uri.startsWith("/plugin")) {
			chain.doFilter(request, response);
			return;
		}




		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(false);
		if(session == null){
			loginErrorInfo(request, response);
			return;
		}

		String username = (String)session.getAttribute(AppStatic.WEB_APP_SESSSION_LOGINNAME);
		if(username == null){
			loginErrorInfo(request, response);
			return;
		}
		chain.doFilter(request, response);
	}


	private void loginErrorInfo(HttpServletRequest request,
								HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();
		String accept = request.getHeader("accept");

		if(accept.matches(".*application/json.*")){// json数据请求
			out.write("{\"status\":false,\"code\":\"101\",\"message\":\"当前会话失效，请重新登录系统!\"}");
		} else {
			// HTML页面
			out.write("<script type='text/javascript'>window.location.href='/admin/login.do?status=timeout';</script>");
		}

		out.flush();
		out.close();
	}


	@Override
	public void init(FilterConfig config) throws ServletException {
//		this.application = config.getServletContext(); 
		logger.debug("mrcms plugin filter initing...");
		
	}

	
	@Override
	public void destroy() {
		logger.debug("mrcms plugin filter destroying...");
		
	}
}

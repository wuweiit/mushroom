package org.marker.mushroom.filter;

import io.github.gitbucket.markedj.Marked;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.utils.FileTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 *  插件过滤器
 * @author marker
 * */
@WebFilter(urlPatterns = "*.md")
public class MarkDownFilter implements Filter {

	/** 日志记录器 */ 
	protected Logger logger =  LoggerFactory.getLogger(MarkDownFilter.class);
	
	
	// 全局作用域
	private ServletContext application;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest  request  = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		response.setCharacterEncoding("UTF-8");
		// 线程绑定请求对象和响应对象
		ActionContext.currentThreadBindRequestAndResponse(request, response);
		
		application.getContextPath();
		    
		String uri = request.getRequestURI();
		 
		String p = WebRealPathHolder.REAL_PATH + uri;
		File file = new File(p);
		if(file.exists()){ 
			String content =FileTools.getFileContet(file, FileTools.FILE_CHARACTER_UTF8);
			String html  =   Marked.marked(content);
			resp.getWriter().write(html);
		}else{
			resp.getWriter().write("<h3>没有找到markdown文档资源，请联系作者编写！</h3>");
		}
		
		resp.getWriter().flush();
		resp.getWriter().close();
	}



	@Override
	public void init(FilterConfig config) throws ServletException {
		this.application = config.getServletContext(); 
		logger.debug("mrcms markdown filter initing...");
		
	}

	
	@Override
	public void destroy() {
		logger.debug("mrcms markdown filter destroying...");
		
	}
}

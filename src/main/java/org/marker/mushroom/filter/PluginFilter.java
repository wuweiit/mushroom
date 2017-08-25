package org.marker.mushroom.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.marker.mushroom.alias.LOG;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.ext.plugin.PluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 插件过滤器
 * 
 *  
 * @author marker
 * */
public class PluginFilter implements Filter {

	/** 日志记录器 */ 
	protected Logger logger =  LoggerFactory.getLogger(LOG.WEBFOREGROUND); 
	
	/** 插件上下文 */
	private PluginContext pluginContext =	PluginContext.getInstance();
	 
	
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest  request  = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		// 线程绑定请求对象和响应对象
		ActionContext.currentThreadBindRequestAndResponse(request, response);
		
		 
		String uri = request.getRequestURI(); 
		int gradient = uri.indexOf("/",7) + 1;  
		if(gradient != -1){
			String pluginUrl = uri.substring(gradient,uri.length());
			String method = request.getMethod();
			logger.info("plugin | {}:/{}",method,pluginUrl);
			try {
				pluginContext.invoke(method, pluginUrl); 
			} catch (Exception e) {
				logger.error("plugin | {}:/{}", method, pluginUrl);
				logger.error("plugin | plugin invoke exception", e); 
			} 
		}
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

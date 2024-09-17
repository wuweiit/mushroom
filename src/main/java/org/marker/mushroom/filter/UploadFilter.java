package org.marker.mushroom.filter;

import com.baidu.ueditor.ActionEnter;
import lombok.extern.slf4j.Slf4j;
import org.marker.mushroom.core.config.impl.StorageConfig;
import org.marker.mushroom.ext.plugin.PluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * 
 * 插件过滤器
 * 
 *  
 * @author marker
 * */
@Slf4j
public class UploadFilter implements Filter {

	/** 日志记录器 */ 
	protected Logger logger =  LoggerFactory.getLogger(UploadFilter.class);
	
	/** 插件上下文 */
	private PluginContext pluginContext =	PluginContext.getInstance();
	 
	
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest  request  = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		ServletContext application = request.getServletContext();
		//		request.setCharacterEncoding( "utf-8" );
		response.setHeader("Content-Type" , "text/html");
		String rootPath = application.getRealPath( "/" );
		/** 系统配置对象 */
		StorageConfig storageConfig = StorageConfig.getInstance();


		String storageType = storageConfig.getStorageType();
		if ("LOCAL_OSS".equals(storageType)) {
			// 获取文件存储地址
			String saveRootPath = storageConfig.getProperties().getProperty("localOss.baseFilePath");
			if (!(saveRootPath != null && !"".equals(saveRootPath))) {
				saveRootPath = rootPath;
			}
			log.debug("rootPath:{}", saveRootPath);

			Writer out = response.getWriter();
			out.write(new ActionEnter(request, rootPath, saveRootPath).exec());
		}
	}



	@Override
	public void init(FilterConfig config) throws ServletException {
//		this.application = config.getServletContext(); 
		logger.debug("mrcms upload filter initing...");
		
	}

	
	@Override
	public void destroy() {
		logger.debug("mrcms upload filter destroying...");
		
	}
}

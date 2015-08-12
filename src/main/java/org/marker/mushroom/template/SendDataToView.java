package org.marker.mushroom.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.marker.develop.freemarker.MessageWrapperModel;
import org.marker.develop.freemarker.ServletContextWrapperModel;
import org.marker.develop.freemarker.SessionWrapperModel;
import org.marker.mushroom.alias.CacheO;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.alias.LOG;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.ext.message.MessageContext;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.template.tags.res.SqlDataSource;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.mushroom.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;






public class SendDataToView {
	
	/** 日志记录对象 */ 
	protected Logger logger =  LoggerFactory.getLogger(LOG.TEMPLATE_ENGINE); 
	
	/** 系统配置信息 */
	private static final SystemConfig syscfg = SystemConfig.getInstance();

	/** 国际化 */
	private static final MessageContext mc = MessageContext.getInstance();
	
	
	private final MyCMSTemplate engine;
	
    public static final String KEY_REQUEST = "Request";
    public static final String KEY_APPLICATION = "Application";
    public static final String KEY_SESSION = "Session";
    public static final String KEY_MESSAGE_CONTEXT = "mrcmsMessageResourceContext";

    
    
	public SendDataToView(MyCMSTemplate engine) {
		this.engine = engine; 
	}

	
	
	
	
	
	/**
	 * 处理数据
	 * 
	 * @param tpl
	 * @throws SystemException
	 */
	public void process(String tpl) throws SystemException {
		Configuration config = engine.config;
		
		

		if(syscfg.isdevMode()){// 模板异常将以HTML格式输出
			config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		}else{// 无错误日志输出
			config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		} 
		
		
		
		HttpServletRequest request   = ActionContext.getReq();
		HttpServletResponse response = ActionContext.getResp();
		ServletContext application   = ActionContext.getApplication();

		
		
		String lang = HttpUtils.getLanguage(request);
		
		
		AllHttpScopesHashModel root = createModel(application, request, config);
		
		
		
		
		
		
		
	
		

		//需要使用的组件准备就绪
		ISupportDao dao = SpringContextHolder.getBean(DAO.COMMON);
		
		List<SqlDataSource> list = engine.getData(tpl);
		
		for(SqlDataSource dataTmp : list){// 一个一个的数据提取策略
			if(dataTmp == null) continue;
		 
			String queryString = dataTmp.getQueryString();
			
			//获取当前栏目
//			Channel current =  (Channel)request.getAttribute(AppStatic.WEB_CURRENT_CHANNEL);
//			queryString = queryString.replaceAll("upid", current.getId()+""); 
			request.setAttribute(dataTmp.getItems(), dao.queryForList(queryString));
					 
			
			
		}
		
		Template template;
		try {
			template = config.getTemplate(tpl);
		} catch (IOException e) {  
			throw new SystemException("获取模板失败：" + tpl);
		}
		Writer writer = null;
		try {
			if(syscfg.isGzip()){// 开启Gzip压缩
				if(WebUtils.checkAccetptGzip(request)){
					OutputStream os = WebUtils.buildGzipOutputStream(response); 
					writer = new OutputStreamWriter(os,"utf-8"); 
				}else{
					writer = response.getWriter();
				}
			}else{
				writer = response.getWriter();
			} 
			template.process(root, writer);
			
			
			
			// 是否启用缓存
			if(syscfg.isStaticPage()){
				EhCacheCacheManager cm =  SpringContextHolder.getBean(CacheO.CacheManager);
				 
				org.springframework.cache.Cache cache = cm.getCache(CacheO.STATIC_HTML);
				
				String path =  "data" + File.separator+"cache" + File.separator +
						lang +File.separator + request.getAttribute("rewriterUrl");
				 
				
				
				File file = new File(WebRealPathHolder.REAL_PATH + path);
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				OutputStream fw = new FileOutputStream(file);
				
				OutputStreamWriter osw = new OutputStreamWriter(fw, "utf-8");
				
				template.process(root, osw); 
				osw.flush();
				osw.close();fw.close();
				
				// lang+"_"+uri
				
				String key = lang + "_" + request.getAttribute("rewriterUrl");
				
				 
				cache.put(key, path);

			}
						
			
		} catch (Exception e) {
			e.printStackTrace();
			new SystemException("发送对象失败");
		}finally{
			if(null != writer){
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					logger.error("response error writer content!", e);
				}
			}
		} 
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	/**
	 * 创建数据模型
	 * @param application
	 * @param request
	 * @param config
	 * @return
	 */
	public AllHttpScopesHashModel createModel(ServletContext application, 
			HttpServletRequest request, Configuration config){
		
		AllHttpScopesHashModel root = new AllHttpScopesHashModel(config.getObjectWrapper(), application, request);
		
		
		// 创建Session数据模型
		SessionWrapperModel sessionModel;
		HttpSession session = request.getSession(false);
		if(session != null) {
            sessionModel = (SessionWrapperModel) session.getAttribute(SessionWrapperModel.ATTR_SESSION_MODEL);
            if (sessionModel == null) {
                sessionModel = new SessionWrapperModel(session, config.getObjectWrapper());
                session.setAttribute(SessionWrapperModel.ATTR_SESSION_MODEL, sessionModel); 
            }
        } else {
        	sessionModel = new SessionWrapperModel(request, config.getObjectWrapper());
        }
        root.putUnlistedModel(KEY_SESSION, sessionModel);
		
        
        // 创建ServletContex数据模型
        ServletContextWrapperModel servletContextModel = new ServletContextWrapperModel(application,config.getObjectWrapper());
        root.putUnlistedModel(KEY_APPLICATION, servletContextModel);
		
		
        // 国际化数据包装模型
		String lang = HttpUtils.getLanguage(request);
        MessageWrapperModel messageModel = new MessageWrapperModel(mc.get(lang), config.getObjectWrapper());
        request.setAttribute(KEY_MESSAGE_CONTEXT, messageModel);
   	    
		return root; 
	}
	
}

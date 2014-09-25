package org.marker.mushroom.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.marker.mushroom.alias.CacheO;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.alias.LOG;
import org.marker.mushroom.alias.UNIT;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.ext.message.MessageContext;
import org.marker.mushroom.ext.plugin.freemarker.EmbedDirectiveInvokeTag;
import org.marker.mushroom.ext.tag.TaglibContext;
import org.marker.mushroom.freemarker.LoadDirective;
import org.marker.mushroom.freemarker.UpperDirective;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.template.tags.res.SqlDataSource;
import org.marker.mushroom.utils.FileTools;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.mushroom.utils.WebUtils;
import org.marker.urlrewrite.freemarker.FrontURLRewriteMethodModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;



/**
 * 模板引擎
 * 
 * 在请求时，实例化（懒加载）
 * 
 * @author marker
 * */
@Service(UNIT.ENGINE_TEMPLATE)
public class MyCMSTemplate {
	
	/** 日志记录对象 */ 
	protected Logger logger =  LoggerFactory.getLogger(LOG.TEMPLATE_ENGINE); 
	
	/** 日志记录对象 */
	private static final Configuration config = new Configuration();
	
	
	private	 static final StringTemplateLoader loader = new StringTemplateLoader();
	
	
	/** 系统配置信息 */
	private static final SystemConfig syscfg = SystemConfig.getInstance();
	
	private static final TaglibContext tagContext = TaglibContext.getInstance();
	
	/** 国际化 */
	private static final MessageContext mc = MessageContext.getInstance();
	
	// 编码集(默认UTF-8)
	public static final String encoding = "utf-8";
	
	// 本地语言(默认汉语)
	public static final Locale locale = Locale.CHINA;
	
	
	

	// 临时存储sql数据引擎
	public List<SqlDataSource> temp;
	
	// 存放模版读取时间，为是否更新JSP提供依据
	private Map<String, TemplateFileLoad> tplCache = Collections.synchronizedMap(new HashMap<String, TemplateFileLoad>());
	
	
	
 
	public MyCMSTemplate(){
		try { 
			config.setDefaultEncoding(encoding);
		    config.setOutputEncoding(encoding);
	        config.setLocale(locale);
	        config.setLocalizedLookup(false);
	        config.setTemplateLoader(loader);//设置模板加载器
		} catch (Exception e) {
			logger.error("template tags init exception!", e);
		}
	}
	
	 
	
	
	
	/**
	 * 代理编译器
	 * @throws SystemException 
	 * @throws IOException 
	 * */
	public void proxyCompile(String tplFileName) throws SystemException, IOException{
		
		// 构造模模版路径
		StringBuilder tplFilePath = new StringBuilder(WebRealPathHolder.REAL_PATH );
		tplFilePath.append("themes").append(File.separator).append(syscfg.get(SystemConfig.THEME_PATH))
		.append(File.separator).append(tplFileName);
		
		File tplFile = new File(tplFilePath.toString());//模板文件 
		
		// 如果模板文件存在 检查是否修改 
		synchronized(this){
			if(syscfg.isdevMode()){//如果是开发模式，每次获取都将会编译
				logger.info("[develop mode] ");
				config.clearTemplateCache();// 清除缓存
				compile(tplFileName, tplFile);
			}else{
				TemplateFileLoad tplModel = tplCache.get(tplFileName);
				if(null != tplModel){
					long rt = tplModel.getReadModified();//获取读取时间
					long mt = tplModel.lastModified();// 获取修改时间
					if (mt > rt) {// 模板文件被修改了滴
						compile(tplFileName, tplFile);
					}
				}else{
					compile(tplFileName,tplFile);
				}
			}
		}

		// 发送
		sendModeltoView(tplFileName);
  
	}
	
	/**
	 * 编译
	 * 此方法相当消耗资源，主要瓶颈在于IO操作
	 * @param 
	 * @throws SystemException 
	 * @throws IOException 
	 * */
	private void compile(String tplFileName, File tplFile) throws SystemException, IOException{
		logger.info("compiling template file " + tplFileName + " to cache");
		
		//第一步：加载模板内容
		TemplateFileLoad tplloader = new TemplateFileLoad(tplFile);
		StringBuilder tplFilePath = new StringBuilder(WebRealPathHolder.REAL_PATH );
		tplFilePath.append("themes").append(File.separator).append("common").append(File.separator).append("function.ftl");
		
		File f = new File(tplFilePath.toString());
		String prefix = FileTools.getFileContet(f, FileTools.FILE_CHARACTER_UTF8);
		
		
		// 创建一个StringBuffer
		String sbc = prefix + tplloader.getContent();
		 
		this.temp = new ArrayList<SqlDataSource>();// 创建此模板页面的数据池
		sbc = replaceTaglib(sbc);// 全部标签解析
		if(syscfg.isStatistics()){
			HttpServletRequest request = ActionContext.getReq();
			String appurl = (String) request.getAttribute(AppStatic.WEB_APP_URL);
			sbc += "<script type=\"text/javascript\" src=\""+appurl+"/public/fetch/main.js\"></script>";
		} 
		tplloader.setSqls(temp);// 设置SQL集合
		 
		if(syscfg.isCompress()){ // 是否开启压缩
			sbc = "<@compress single_line=true>"+sbc+"</@compress>";
		}
		
		// 向模板加载器中写入模板信息 
		loader.putTemplate(tplFileName, sbc);
		tplCache.put(tplFileName, tplloader);
		this.temp = null;
	}
	
	
 
	
	
	/**
	 * 将对象传递到view
	 * */
	public void sendModeltoView(String tpl) throws SystemException{
		
		if(syscfg.isdevMode()){// 模板异常将以HTML格式输出
			config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		}else{// 无错误日志输出
			config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		} 
		
//		new SendDataToView(tpl).process();
		HttpServletRequest request      = ActionContext.getReq();
		HttpServletResponse response     = ActionContext.getResp();
		ServletContext application  = ActionContext.getApplication();

		Map<String,Object> root = new HashMap<String,Object>(); 
		 
		String lang = HttpUtils.getLanguage(request);
		
		request.setAttribute("mrcmsMessageResourceContext", mc.get(lang));
	    
		
		root.put("encoder", new FrontURLRewriteMethodModel());//URL重写  
		root.put("list",  new UpperDirective());// 调用
		root.put("load", new LoadDirective());//
		root.put("plugin", new EmbedDirectiveInvokeTag());// 嵌入式指令插件
		
		@SuppressWarnings("unchecked")
		Enumeration<String> attrs3 = application.getAttributeNames(); 
		while (attrs3.hasMoreElements()) {
			String attrName = attrs3.nextElement();
			root.put(attrName, application.getAttribute(attrName));
		}
		//转移Session数据
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Enumeration<String> attrs2 = session.getAttributeNames();
		while (attrs2.hasMoreElements()) {
			String attrName = attrs2.nextElement();
			root.put(attrName, session.getAttribute(attrName));
		}
		//这里是进行数据转移
		@SuppressWarnings("unchecked")
		Enumeration<String> attrs = request.getAttributeNames(); 
		while (attrs.hasMoreElements()) {
			String attrName = attrs.nextElement();
			root.put(attrName, request.getAttribute(attrName));
		}

		//需要使用的组件准备就绪
		ISupportDao dao = SpringContextHolder.getBean(DAO.COMMON);
		
		
		for(SqlDataSource dataTmp : getData(tpl)){// 一个一个的数据提取策略
			if(dataTmp == null) continue;
		 
			String queryString = dataTmp.getQueryString();
			
			//获取当前栏目
//			Channel current =  (Channel)request.getAttribute(AppStatic.WEB_CURRENT_CHANNEL);
//			queryString = queryString.replaceAll("upid", current.getId()+""); 
			root.put(dataTmp.getItems(), dao.queryForList(queryString));
					 
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
				CacheManager cm =  SpringContextHolder.getBean(CacheO.CacheManager);
				 
				Cache cache = cm.getCache(CacheO.STATIC_HTML);
				
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
				
				cache.put(new Element(key, path));

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
	 * 标记库替换
	 * @param content 内容
	 * @return String 
	 * @throws SystemException 
	 * */
	private String replaceTaglib(String content) throws SystemException{
		return tagContext.execute(content); 
	}



	/**
	 * 推送数据引擎到模版引擎
	 * 
	 * @param items 传递名称
	 * @param data2 数据引擎
	 * @throws SystemException 
	 * */
	public void put(SqlDataSource sqlDs) throws SystemException {
		sqlDs.generateSql();//生成SQL语句
		if(null != temp) temp.add(sqlDs);
	}



	/**
	 * 获取数据引擎集合
	 * @return Map<String,SQLDataEngine> 集合
	 * */
	public List<SqlDataSource> getData(String tpl){
		if(tplCache.containsKey(tpl)){
			return tplCache.get(tpl).getSqls();
		}
		return new ArrayList<SqlDataSource>(0);
	}
 
 
	/**
	 * 清除缓存
	 */
	public void clearCache(){ 
		tplCache.clear();
		config.clearTemplateCache();
	}
}

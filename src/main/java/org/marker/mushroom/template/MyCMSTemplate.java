package org.marker.mushroom.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;
import org.marker.app.utils.ConfigurationHelper;
import org.marker.mushroom.alias.Core;
import org.marker.mushroom.alias.LOG;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.message.MessageContext;
import org.marker.mushroom.ext.plugin.freemarker.EmbedDirectiveInvokeTag;
import org.marker.mushroom.ext.tag.TaglibContext;
import org.marker.mushroom.freemarker.*;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.template.tags.res.SqlDataSource;
import org.marker.mushroom.utils.FileTools;
import org.marker.urlrewrite.freemarker.FrontURLRewriteMethodModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * 模板引擎
 * 
 * 在请求时，实例化（懒加载）
 * 
 * @author marker
 * */
@Service(Core.ENGINE_TEMPLATE)
public class MyCMSTemplate {
	
	/** 日志记录对象 */ 
	protected Logger logger =  LoggerFactory.getLogger(LOG.TEMPLATE_ENGINE); 
	
	// freemarker配置
	public final Configuration config = new Configuration();
	
	// 编码集(默认UTF-8)
	public static final String encoding = "utf-8";
	
	// 本地语言(默认汉语)
	public static final Locale locale = Locale.CHINA;
	
	private static final StringTemplateLoader loader = new StringTemplateLoader();
	
	/** 系统配置信息 */
	
	private final TaglibContext tagContext = TaglibContext.getInstance();

	
	@Autowired
	private SystemConfig syscfg;

	// 临时存储sql数据引擎
	public List<SqlDataSource> temp;
	
	// 存放模版读取时间，为是否更新JSP提供依据
	private Map<String, TemplateFileLoad> tplCache = Collections.synchronizedMap(new HashMap<String, TemplateFileLoad>());
	
	
	public MyCMSTemplate(){
		logger.info(">>>>> TemplateEngine init... ");
//		FreeMarkerConfigurer free = new FreeMarkerConfigurer();
//		this.config = free.getConfig();
//		config.setTemplateLoader(loader);//设置模板加载器
//        
		
		config.setSharedVariable("load", new LoadDirective());
		config.setSharedVariable("Boostrap3Nav", new BootStrap3NavDirective());// 导航菜单
        config.setSharedVariable("HuaXiSiYuanNav", new HuaxiSiYuanNavDirective());// 导航菜单(mobile)
        config.setSharedVariable("HuaXiSiYuanPCNav", new HuaxiSiYuanPCNavDirective());// 导航菜单(PC)


		config.setSharedVariable("Nav",new NavDirective());// 通用导航指令
		config.setSharedVariable("NavChild",new NavChildDirective());// 通用二级导航指令


		config.setSharedVariable("encoder", new FrontURLRewriteMethodModel());//URL重写
		config.setSharedVariable("plugin", new EmbedDirectiveInvokeTag());// 嵌入式指令插件
		config.setSharedVariable("Page", new PageDirective());// 分页数据
		try {  
			config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
			config.setDefaultEncoding(encoding);
		    config.setOutputEncoding(encoding);
			config.setEncoding(locale, encoding);// 
	        config.setLocale(locale);
	        config.setLocalizedLookup(false);
	        config.setTemplateLoader(loader);// 
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
		try {
			config.setSharedVariable("req", ActionContext.getReq());
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}

		// 配置了制定的主题路径
		String themesPath = syscfg.getThemesPath();
		// 构造模模版路径
		StringBuilder tplFilePath = new StringBuilder( themesPath);
		tplFilePath.append(File.separator).append(syscfg.getThemeActive())
		.append(File.separator).append(tplFileName);
		
		File tplFile = new File(tplFilePath.toString());//模板文件 
		
		logger.error(tplFile.getPath());
		
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
		TemplateFileLoad tplloader = null;
		try{
			tplloader = new TemplateFileLoad(tplFile);
		}catch (FileNotFoundException e){
			throw new FileNotFoundException(tplFileName);
		}

        String themesPath = syscfg.getThemesPath();
		StringBuilder tplFilePath = new StringBuilder(themesPath );
		tplFilePath.append(File.separator).append("common").append(File.separator).append("function.ftl");
		
		File f = new File(tplFilePath.toString());
		String prefix = FileTools.getFileContet(f, FileTools.FILE_CHARACTER_UTF8);
		
		
		// 创建一个StringBuffer
		String sbc = prefix + tplloader.getContent();
		this.temp = new ArrayList<SqlDataSource>();// 创建此模板页面的数据池
		sbc = replaceTaglib(sbc);// 全部标签解析


        if(syscfg.isdevMode()){
            System.out.println("=================【输出Freemarker模板】==================");
            System.out.println(sbc);
            System.out.println("=================【输出Freemarker模板 结束】==================");
        }

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
		return new ArrayList<>(0);
	}
 
 
	/**
	 * 清除缓存
	 */
	public void clearCache(){ 
		tplCache.clear();
		config.clearTemplateCache();
	}
}

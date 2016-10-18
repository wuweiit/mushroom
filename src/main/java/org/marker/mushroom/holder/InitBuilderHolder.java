package org.marker.mushroom.holder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.marker.ext.Msei;
import org.marker.ext.module.ModuleContext;
import org.marker.mushroom.alias.LOG;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebAPP;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.core.proxy.SingletonProxyKeyWordComputer;
import org.marker.mushroom.ext.message.MessageContext;
import org.marker.mushroom.ext.tag.TaglibContext;
import org.marker.mushroom.ext.tag.impl.AbsoluteURLTagImpl;
import org.marker.mushroom.ext.tag.impl.ExecuteTimeTagImpl;
import org.marker.mushroom.ext.tag.impl.FormatDateTagImpl;
import org.marker.mushroom.ext.tag.impl.IfTagImpl;
import org.marker.mushroom.ext.tag.impl.ListTagImpl;
import org.marker.mushroom.ext.tag.impl.LoopTagImpl;
import org.marker.mushroom.ext.tag.impl.OnlineUsersTagImpl;
import org.marker.mushroom.ext.tag.impl.PluginTagImpl;
import org.marker.mushroom.ext.tag.impl.SingleCategoryTagImpl;
import org.marker.mushroom.ext.tag.impl.SqlExecuteTagImpl;
import org.marker.mushroom.ext.tag.impl.StringSubTagImpl;
import org.marker.mushroom.ext.tag.impl.URLRewriteTagImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;



/**
 * 环境初始化构建
 * 
 * 
 * @author marker
 * */
public class InitBuilderHolder implements ServletContextAware{
	
	/** 日志记录器 */ 
	protected Logger logger =  LoggerFactory.getLogger(LOG.WEBFOREGROUND); 
	
	
	@Override
	public void setServletContext(ServletContext application) {
    	String webRootPath = application.getRealPath(File.separator)+File.separator;//网站根目录路径
    	logger.info("mrcms runtime on path = {}", webRootPath);	
    	
    	logger.info("check mrcms whether install?");
    	WebAPP.install = isInstall(webRootPath);// 设置系统是否被安装
    	logger.info("check success. install = {}", WebAPP.install);
    	
    	
    	
		/* 
		 * ============================================================
		 *          ActionContext bind (application)应用作用域
		 * ============================================================
		 */ 	
    	logger.info("bind application context = {}", application);	
    	ActionContext.currentThreadBindServletContext(application);
    	
    	
    	
		/* 
		 * ============================================================
		 *                初始化系统配置信息路径
		 * ============================================================
		 */
    	SystemConfig systemConfig = SystemConfig.getInstance();
    	logger.info("build systemConfig instance = {}", systemConfig);	
    	application.setAttribute(AppStatic.WEB_APP_CONFIG, systemConfig.getProperties());
    	
    	
    	
		/* 
		 * ============================================================
		 *               URLRewrite 初始化URL规则
		 * ============================================================
		 */
    	URLRewriteConfig urlConfig = URLRewriteConfig.getInstance();
    	logger.info("build URL-rewriteConfig instance = {}", urlConfig);	
    	
    	
    	
    	/* 
		 * ============================================================
		 *               关键字提取代理，初始化(避免懒加载带来的等待)
		 * ============================================================
		 */
    	logger.info("build keyword instance = {}", "");	
    	SingletonProxyKeyWordComputer.init(webRootPath);// 初始化dic
    	SingletonProxyKeyWordComputer.getInstance();
    	
    	
    	
		/* 
		 * ============================================================
		 *                       模块的初始化
		 * ============================================================
		 */ 
    	logger.info("mrcms loading module ...");
		String moduleDir = webRootPath+"modules";// 模块目录
		
		// 缓存目录
		String cacheDir = moduleDir + File.separator + "cache";// 模块目录
		
		Map<String,String> configMap = new HashMap<String,String>();
	   	// 自动部署目录配置
        configMap.put(Msei.DIR_MODULES, moduleDir);
        // 缓存目录
        configMap.put(Msei.DIR_CACHE, cacheDir); 
        // 日志级别
        configMap.put(Msei.LOG_LEVEL, "1"); 
        
		try {
			Msei msei = new Msei(configMap); 
	        ModuleContext context = msei.getModuleContext(); 
	        application.setAttribute(Msei.MODULE_CONTEXT, context); 
		} catch (Exception e) {
			logger.error("", e);
		} 
    	
    	
    	
    	/* 
		 * ============================================================
		 *               TaglibContext 初始化
		 * ============================================================
		 */
    	logger.info("mrcms taglibs init ...");
    	TaglibContext taglibs = TaglibContext.getInstance();
    	
    	// 系统内置标签
    	taglibs.put(new AbsoluteURLTagImpl());
    	taglibs.put(new ExecuteTimeTagImpl());
    	taglibs.put(new FormatDateTagImpl());
    	taglibs.put(new IfTagImpl());
    	taglibs.put(new ListTagImpl());
    	taglibs.put(new LoopTagImpl());
    	taglibs.put(new OnlineUsersTagImpl());
    	taglibs.put(new PluginTagImpl());
    	taglibs.put(new SqlExecuteTagImpl());
    	taglibs.put(new StringSubTagImpl());
    	taglibs.put(new URLRewriteTagImpl());
    	taglibs.put(new SingleCategoryTagImpl());
    	logger.info("mrcms taglibs init complete");
    	
    	
    	
    	
    	/* 
		 * ============================================================
		 *               MessageContext 初始化
		 * ============================================================
		 */
    	logger.info("mrcms MessageContext init ...");
    	MessageContext mc = MessageContext.getInstance();
    	try {
			mc.reday();
		} catch (Exception e) {
			logger.error("", e);
		} 
    	 
    	
    	logger.info("init Building complete");
    	
	}
	
	
	/**
	 * 判断是否是否已安装(true:安装 false:未安装)
	 * @return boolean 状态
	 * */
	private boolean isInstall(String webRootPath){
		return new File(webRootPath + "data" + File.separator + "install.lock").exists();
	}

}

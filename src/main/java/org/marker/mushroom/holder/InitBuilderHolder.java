package org.marker.mushroom.holder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.marker.mushroom.alias.LOG;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebAPP;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.core.proxy.SingletonProxyKeyWordComputer;
import org.marker.mushroom.ext.message.MessageContext;
import org.marker.mushroom.ext.message.MessageDBContext;
import org.marker.mushroom.ext.model.ContentModelContext;
import org.marker.mushroom.ext.model.impl.ArticleModelImpl;
import org.marker.mushroom.ext.model.impl.CategoryModelImpl;
import org.marker.mushroom.ext.model.impl.ContentModelImpl;
import org.marker.mushroom.ext.model.impl.DoctorModelImpl;
import org.marker.mushroom.ext.plugin.PluginContext;
import org.marker.mushroom.ext.plugin.Pluginlet;
import org.marker.mushroom.ext.plugin.impl.GuestBookPluginletImpl;
import org.marker.mushroom.ext.tag.TaglibContext;
import org.marker.mushroom.ext.tag.impl.*;
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
    	String webRootPath = application.getRealPath(File.separator);//网站根目录路径
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
		 *          Database config bind
		 * ============================================================
		 */
		DataBaseConfig.getInstance().init();
    	
    	
		/* 
		 * ============================================================
		 *                初始化系统配置信息路径
		 * ============================================================
		 */

		SystemConfig syscfg = SpringContextHolder.getBean("systemConfig");
    	logger.info("build systemConfig instance = {}", syscfg);
    	application.setAttribute(AppStatic.WEB_APP_CONFIG, syscfg.getProperties());
    	
    	
    	
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
		 *               内容模型支持
		 * ============================================================
		 */
		ContentModelContext contentModelContext = ContentModelContext.getInstance();

        contentModelContext.put(new ContentModelImpl());// 栏目数据模型
		contentModelContext.put(new ArticleModelImpl());// 文章数据模型
		contentModelContext.put(new DoctorModelImpl());// 医生数据模型
        contentModelContext.put(new CategoryModelImpl());// 科室数据模型







        PluginContext pluginContext = PluginContext.getInstance();

        try {
            pluginContext.put(new GuestBookPluginletImpl());
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
		taglibs.put(new ChildChannelTagImpl());
		taglibs.put(new ListCategoryTagImpl());

    	logger.info("mrcms taglibs init complete");
    	
    	
    	
    	
    	/* 
		 * ============================================================
		 *               MessageContext 初始化
		 * ============================================================
		 */
    	logger.info("mrcms MessageContext init ...");
        MessageDBContext messageDBContext = MessageDBContext.getInstance();

    	try {
            messageDBContext.init();
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

package org.marker.mushroom.controller;

import com.mchange.lang.StringUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.alias.CacheO;
import org.marker.mushroom.alias.Core;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.ext.message.MessageContext;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.template.MyCMSTemplate;
import org.marker.mushroom.utils.FileTools;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.security.Base64;
import org.marker.security.DES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * 系统配置管理控制器
 *
 * @author marker
 *
 * @date 2016-10-22 14:39:20
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 *
 * */
@Controller
@RequestMapping("/admin/system")
public class SystemController extends SupportController {
	
	/** 日志记录对象 */ 
	protected Logger logger =  LoggerFactory.getLogger(SystemController.class); 

	/** 系统配置对象 */
	private SystemConfig config = SystemConfig.getInstance();



    /**
     * 默认构造
     */
	public SystemController() {
		this.viewPath = "/admin/system/";
	}
	 
	
	// 网站基本信息
	@RequestMapping("/siteinfo")
	public String siteinfo(HttpServletRequest request){
		request.setAttribute("config", config.getProperties());
		MessageContext mc = MessageContext.getInstance();
		request.setAttribute("langselect", mc.getReadySelectElement());
		return this.viewPath + "siteinfo";
	}
	
	
	//保存网站配置信息
	@ResponseBody
	@RequestMapping("/saveinfo")
	public Object saveinfo(HttpServletRequest request){ 
		try{ 
			/* 判断统计是否修改 */
			MyCMSTemplate cmstemplate = SpringContextHolder.getBean(Core.ENGINE_TEMPLATE); 
			String new_statistics = request.getParameter("config.statistics");
			if(!config.get(SystemConfig.STATISTICS).equals(new_statistics)){
				cmstemplate.clearCache(); 
			}
			/* 判断主题是否切换 */
			String new_themes_path = request.getParameter("config.themes_path");
			if(!config.get(SystemConfig.THEME_PATH).equals(new_themes_path)){
				cmstemplate.clearCache(); 
			}
			/* 判断启用代码压缩是否切换 */
			String new_compress = request.getParameter("config.compress");
			if(!config.get(SystemConfig.COMPRESS).equals(new_compress)){
				cmstemplate.clearCache(); 
			}
			
			/* 清除EHCache 缓存数据，并没有清除静态文件哦 */
			String new_statichtml = request.getParameter("config.statichtml");
			if(Boolean.valueOf(new_statichtml)){
				EhCacheCacheManager cm =  SpringContextHolder.getBean(CacheO.CacheManager); 
				org.springframework.cache.Cache cache = cm.getCache(CacheO.STATIC_HTML); 
				cache.clear();
			}
			/* 切换默认语言*/
            String newDefaultLang = request.getParameter("config.defaultlang");
            String oldDefaultLang = config.get(SystemConfig.DEFAULTLANG);

            if(!oldDefaultLang.equals(newDefaultLang)){




            }
			
			
			/* 系统基本信息配置 */
			config.set("title", request.getParameter("config.title"));//网站标题
			config.set("url", request.getParameter("config.url"));//网站地址
			config.set("keywords", request.getParameter("config.keywords"));//网站关键字
			config.set("description", request.getParameter("config.description"));//网站描述
			config.set("mastermail", request.getParameter("config.mastermail"));//管理员邮箱
			config.set("copyright", request.getParameter("config.copyright"));//版权信息
			config.set("icp", request.getParameter("config.icp"));//ICP备案 
			config.set(SystemConfig.STATISTICS, request.getParameter("config.statistics"));// 是否启用统计
			config.set(SystemConfig.DEFAULTLANG, request.getParameter("config.defaultlang"));// 默认语言
			
			
			/* 主题配置 */
			config.set("index_page", request.getParameter("config.index_page"));//网站首页
			config.set("error_page", request.getParameter("config.error_page"));//错误模版
			config.set(SystemConfig.THEME_PATH, request.getParameter("config.themes_path"));//主题路径
			config.set("themes_cache", request.getParameter("config.themes_cache"));//主题缓存目录
			config.set(SystemConfig.DEV_MODE, request.getParameter("config.dev_mode"));//是否开发模式
			config.set(SystemConfig.GZIP, request.getParameter("config.gzip"));//GZIP
			config.set(SystemConfig.COMPRESS, request.getParameter("config.compress"));//GZIP
			config.set(SystemConfig.STATIC_PAGE, request.getParameter("config.statichtml"));// 页面静态化
			
			
			config.store();//修改配置信息状态
			return new ResultMessage(true, "更新成功!");
		}catch (Exception e) {
			logger.error("", e);
			return new ResultMessage(false, "更新失败!");
		} 
	}
	
	

	
	
	/**
	 * SEO设置
	 * */
	@RequestMapping("/seoinfo")
	public String seoinfo(HttpServletRequest request){
		URLRewriteConfig urlRewriteConfig =  URLRewriteConfig.getInstance();
		request.setAttribute("urlConfig", urlRewriteConfig.getProperties());
		return this.viewPath + "seoinfo";
	}
	
	@ResponseBody
	@RequestMapping("/saveseoinfo")
	public Object saveseoinfo(HttpServletRequest request){
		URLRewriteConfig urlRewriteConfig =  URLRewriteConfig.getInstance();
		try{
			String channelRule = request.getParameter("url.channel");
			String contentRule = request.getParameter("url.content");
			String pageRule    = request.getParameter("url.page");
			String pageSuffix  = request.getParameter("page.suffix");
			
			urlRewriteConfig.set(URLRewriteConfig.URL_CHANNEL, channelRule);
			urlRewriteConfig.set(URLRewriteConfig.URL_CONTENT, contentRule);
			urlRewriteConfig.set(URLRewriteConfig.URL_PAGE, pageRule);
			urlRewriteConfig.set(URLRewriteConfig.PAGE_SUFFIX, pageSuffix);
			
			urlRewriteConfig.store();
			return new ResultMessage(true, "更新成功!");
		}catch (Exception e) {
			log.error("更新url重写失败了!", e);
			return new ResultMessage(true, "更新失败!");
		}
	}
	
	
	/**
	 * Mail配置
	 * */
	@RequestMapping("/mailinfo")
	public String mailinfo(HttpServletRequest request){ 
		return this.viewPath + "mailinfo";
	} 
	
	
	
	/**
	 * 缓存
	 * */
	@RequestMapping("/cache")
	public String cache(HttpServletRequest request){ 
		return this.viewPath + "cache";
	} 
	
	/**
	 * 进入数据库配置
	 * @param request
	 * @return
	 */
	@RequestMapping("/dbinfo")
	public ModelAndView dbinfo(HttpServletRequest request){
		ModelAndView view = new ModelAndView(this.viewPath + "dbinfo");
		DataBaseConfig dbconfig = DataBaseConfig.getInstance();
		Properties config = (Properties) dbconfig.getProperties().clone();
 
		String pass = config.getProperty("mushroom.db.pass");
		
		String desPass = getDesCode(pass);
		config.setProperty("mushroom.db.pass", desPass);
		
		view.addObject("sql", config);
		return view;
	}
	/**
	 * 获取Des加密结果
	 * */
	private String getDesCode(String pass){
		String key = SystemConfig.getInstance().get("secret_key");//网站秘钥，这是在安装的时候获取的
		try {
			return Base64.encode(DES.encrypt(pass.getBytes(), key));
		} catch (Exception e) { e.printStackTrace();}
		return pass;
	}
	
	
	
	/** 保存数据库配置  */
	@ResponseBody
	@RequestMapping("/savedbinfo")
	public Object savedbinfo(HttpServletRequest request){
		DataBaseConfig config = DataBaseConfig.getInstance();
		String oldPass = config.get("mushroom.db.pass");
		String newpass = request.getParameter("sql.pass");
		
		if(!getDesCode(oldPass).equals(newpass)){//修改密码了
			oldPass = newpass;
		}
		
		try{
			//数据库连接配置信息
			config.set("mushroom.db.host", request.getParameter("sql.host"));
			config.set("mushroom.db.port", request.getParameter("sql.port"));
			config.set("mushroom.db.demo", request.getParameter("sql.demo"));
			config.set("mushroom.db.char", request.getParameter("sql.char"));
			config.set("mushroom.db.debug", request.getParameter("sql.debug"));
//			config.set("mushroom.db.prefix", request.getParameter("sql.prefix"));
			config.set("mushroom.db.driver", request.getParameter("sql.driver"));
			config.set("mushroom.db.user", request.getParameter("sql.user"));
			config.set("mushroom.db.pass", oldPass);
			
			//数据库连接池配置信息
			config.set("c3p0.initialPoolSize", request.getParameter("sql.initialPoolSize"));
			config.set("c3p0.minPoolSize", request.getParameter("sql.minPoolSize"));
			config.set("c3p0.maxPoolSize", request.getParameter("sql.maxPoolSize"));
			config.set("c3p0.acquireIncrement", request.getParameter("sql.acquireIncrement"));
			config.set("c3p0.maxIdleTime", request.getParameter("sql.maxIdleTime"));
			config.set("c3p0.maxStatements", request.getParameter("sql.maxStatements"));
			
			config.store();//持久化配置信息
			return new ResultMessage(true, "修改成功! 重启服务器生效!");
		}catch (Exception e) {
			log.error("save db config faild!", e);
		}
		return new ResultMessage(false, "更新失败!");
	}
	
	
	
	/**
	 * 
	 * REST
	 * 
	 * @return 
	 * 
	 */
	@RequestMapping("/themes")
	public @ResponseBody Object themes(HttpServletRequest request){ 
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String themesPath = WebRealPathHolder.REAL_PATH + File.separator+"themes"; 
		File file = new File(themesPath); 
		String[] filelist = file.list();
		for(String themeName : filelist ){
			String config = themesPath + File.separator +themeName+File.separator+"config.groovy";
			
			try {
				
				File f = new File(config);
				if(f.exists()){// 如果配置文件存在 
					String groovyScript = FileTools.getFileContet(f, FileTools.FILE_CHARACTER_UTF8);
					
					 
					Binding bind = new Binding(); 
					GroovyShell gs = new GroovyShell(bind);
					gs.evaluate(groovyScript); 
					
					@SuppressWarnings("unchecked")
					Map<String, String> themecfg = (Map<String, String>) bind.getVariable("_config");
					
					 
					String icon = themecfg.get("icon");
					
					
					String website = HttpUtils.getRequestURL(request);
					
					String iconpath = website+"/themes/"+themeName+"/"+icon;
					 
					themecfg.put("icon", iconpath);
					themecfg.put("path", themeName); 
					list.add(themecfg);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} 
		return list; 
		
	}

	
	

}

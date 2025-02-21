package org.marker.mushroom.system.controller;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.config.impl.StorageConfig;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.FileTools;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.security.Base64;
import org.marker.security.DES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 *
 * 存储配置
 *
 * @author marker
 *
 * @date 2016-10-22 14:39:20
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 *
 * */
@Slf4j
@Controller
@RequestMapping("/admin/system/storage")
public class StorageController extends SupportController {

	/** 系统配置对象 */
	@Autowired
	private SystemConfig config;


	@Resource
	private StorageConfig storageConfig;

	@GetMapping("/info")
	public void getStorageInfo(HttpServletRequest request){
		request.setAttribute("config", storageConfig.getProperties());
	}
	
	
	//保存网站配置信息
	@ResponseBody
	@PostMapping("/info")
	public Object saveStorageInfo(StorageConfig storageConfigParams, HttpServletRequest request){
		try {
			/* 系统存储配置 */
			storageConfigParams.storeAsync();

			// TODO OSS Client重建


			return new ResultMessage(true, "更新成功!");
		} catch (Exception e) {
			log.error("存储配置更新失败", e);
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
		Properties configClone = (Properties) dbconfig.getProperties().clone();
 
		String pass = configClone.getProperty("mushroom.db.pass");
		String desPass = getDesCode(pass);
		configClone.setProperty("mushroom.db.pass", desPass);
		
		view.addObject("sql", configClone);
		return view;
	}




	@Resource
	private SystemConfig syscfg;
	/**
	 * 获取Des加密结果
	 * */
	private String getDesCode(String pass){
		String key = syscfg.get("secret_key");//网站秘钥，这是在安装的时候获取的
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
	 * 获取网站主题列表
	 *
	 */
	@RequestMapping("/themes")
	public @ResponseBody Object themes(HttpServletRequest request){ 
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();


		String themesPath = config.getThemesPath();
        File file = new File(themesPath);
        String[] filelist = file.list();

		for(String themeName : filelist ){

			String config = themesPath + File.separator +themeName+File.separator  + "config.groovy";
			
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

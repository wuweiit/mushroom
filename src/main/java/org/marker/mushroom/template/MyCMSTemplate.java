package org.marker.mushroom.template;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.alias.Core;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.TaglibContext;
import org.marker.mushroom.freemarker.config.WebFreeMarkerConfigurer;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.template.tags.res.WebDataSource;
import org.marker.mushroom.utils.FileTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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
	protected Logger logger =  LoggerFactory.getLogger(MyCMSTemplate.class);
	
	// freemarker配置
	public Configuration config;
	
	// 编码集(默认UTF-8)
	public static final String encoding = "utf-8";
	
	// 本地语言(默认汉语)
	public static final Locale locale = Locale.CHINA;
	
//	private static final StringTemplateLoader loader = new StringTemplateLoader();
	
	/** 系统配置信息 */
	
	private final TaglibContext tagContext = TaglibContext.getInstance();

	


	// 临时存储sql数据引擎
	public List<WebDataSource> temp;
	
	// 存放模版读取时间，为是否更新JSP提供依据
	private Map<String, TemplateFileLoad> tplCache = Collections.synchronizedMap(new HashMap<String, TemplateFileLoad>());
	
	
	public MyCMSTemplate(){
		logger.info(">>>>> TemplateEngine init... ");
        WebFreeMarkerConfigurer freeMarkerConfigurer = SpringContextHolder.getBean("webFrontConfiguration");
        config = freeMarkerConfigurer.getConfiguration();
	}


	
	/**
	 * 代理编译器
	 * @throws SystemException 
	 * @throws IOException 
	 * */
	public void proxyCompile(String tplFileName) throws SystemException, IOException{
		SystemConfig syscfg = SystemConfig.getInstance();
		try {
			config.setSharedVariable("req", ActionContext.getReq());
		} catch (TemplateModelException e) {
			logger.error("", e);
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
		SystemConfig syscfg = SystemConfig.getInstance();
		
		//第一步：加载模板内容
		TemplateFileLoad tplloader = null;
		try {
			tplloader = new TemplateFileLoad(tplFile);
		} catch (FileNotFoundException e){
			throw new FileNotFoundException(tplFile.getAbsolutePath());
		}

        String themesPath = syscfg.getThemesPath();// 主题文件夹




        StringBuilder templateContent = tplloader.getContentBuffer();


        // 通用函数（freemarker宏）头部
		StringBuilder tplFilePath = new StringBuilder(themesPath )
                .append(File.separator).append("common").append(File.separator).append("function.ftl");

		File functionFile = new File(tplFilePath.toString());
		if(functionFile.exists()){
            String functionTemplateString = FileTools.getFileContet(functionFile, FileTools.FILE_CHARACTER_UTF8);
            // 插入到头部
            templateContent.insert(0,functionTemplateString);
        }
		
		
		// 创建一个StringBuffer
		this.temp = new ArrayList< >(); // 创建此模板页面的数据池
		String sbc = replaceTaglib(templateContent.toString());// 全部标签解析
        StringBuilder templateStringBuilder = new StringBuilder(sbc);


        // 开发模式输出模板内容到目录里
        if(syscfg.isdevMode()){
            String themesCache = syscfg.getThemesCache();
            File file = new File(WebRealPathHolder.REAL_PATH + themesCache + File.separator + tplFileName);
            if(!file.exists()){
                file.getParentFile().mkdirs();
            }
            FileTools.setFileContet(file, sbc, FileTools.FILE_CHARACTER_UTF8);
        }

		if(syscfg.isStatistics()){// 是否开启站内统计
			HttpServletRequest request = ActionContext.getReq();
			String appurl = (String) request.getAttribute(AppStatic.WEB_APP_URL);
            templateStringBuilder.append("<script type=\"text/javascript\" src=\"")
                    .append(appurl)
                    .append("/public/fetch/main.js\"></script>");
		}

        if(syscfg.isCompress()){ // 是否开启压缩
            templateStringBuilder.insert(0, "<@compress single_line=true>")
                    .append("</@compress>");
        }

        /** 三方统计 */
        templateStringBuilder.append(syscfg.getTongjiScript());




        HttpServletRequest request = ActionContext.getReq();
        String userAgent = request.getHeader("User-Agent");
        // 检查为百度蜘蛛
        if(!StringUtils.isEmpty(userAgent) && userAgent.indexOf("Baiduspider") != -1){
            templateStringBuilder.append(Core.COPYRIGHT);
        }

		tplloader.setSqls(temp);// 设置SQL集合
		
		// 向模板加载器中写入模板信息
        StringTemplateLoader loader = SpringContextHolder.getBean("stringTemplateLoader");

		loader.putTemplate(tplFileName, templateStringBuilder.toString());
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
	public void put(WebDataSource sqlDs) throws SystemException {
		sqlDs.generateSql(); // 生成SQL语句
		if(null != temp) temp.add(sqlDs);
	}



	/**
	 * 获取数据引擎集合
	 * @return Map<String,SQLDataEngine> 集合
	 * */
	public List<WebDataSource> getData(String tpl){
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

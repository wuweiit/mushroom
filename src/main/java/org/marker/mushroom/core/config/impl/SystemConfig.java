package org.marker.mushroom.core.config.impl;


import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.core.config.ConfigDBEngine;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * 系统配置类（对Properties进行了简单封装）
 * 用于配置系统配置文件，提供读取和保存两种持久化操作
 * 在系统StartListener监听器中进行配置文件地址的初始化
 * @author marker
 * */
public final class SystemConfig extends ConfigDBEngine {


	
	
	// 开发模式
	public static final String DEV_MODE =  "dev_mode";
	
	// 主题文件夹
	public static final String THEMES_PATH = "themes_path";

	// 当前使用的主题
	public static  final  String THEMES_ACTIVE = "themes_active";

	/** 主题缓存目录 */
	public static final String THEMES_CACHE = "themes_cache";
	
	// 是否开启动态HTML的GZIP
	public static final String GZIP = "gzip";
	
	// 是否启用站内统计
	public static final String STATISTICS = "statistics";
	
	// 是否启用代码压缩
	public static final String COMPRESS = "compress";
	
	// 默认语言
	public static final String DEFAULTLANG = "defaultlang";
	
	// 页面静态化
	public static final String STATIC_PAGE = "statichtml";
	
	// 主页地址
	public static final String HOME_PAGE = "index_page";

	// 文件存储路径
	public static final String FILE_PATH = "file_path";
	// 登录路径配置
	public static final String SYSTEM_LOGIN_SAFE = "system.login.safe";
	
	

	private static SystemConfig systemConfig;


	/**
	 * 初始化就读取配置文件哦
	 *
	 * @param jdbcTemplate
	 */
	private SystemConfig(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}


	/**
	 * 获取实例
	 * @return SystemConfig
	 */
	public static SystemConfig getInstance() {
		if(systemConfig == null){
			synchronized (SystemConfig.class){
				if(systemConfig == null){
					JdbcTemplate jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");
					systemConfig = new SystemConfig(jdbcTemplate);
				}
			}
		}
		return systemConfig;
	}



	/**
	 * 配置文件中属性名称配置
	 * */
	public interface Names{
		/** 关键字 */
		String KEYWORDS = "keywords";
		/** 描述信息 */
		String DESCRIPTION = "description";
	}


	/**
	 * 开发模式
	 * @return
	 */
	public boolean isdevMode() {
		String value = this.properties.get(DEV_MODE).toString();
		return Boolean.valueOf(value);
	}
	
	/**
	 * 是否启用统计
	 * @return
	 */
	public boolean isStatistics() {
		String value = this.properties.get(STATISTICS).toString(); 
		return Boolean.valueOf(value); 
	}

	
	/**
	 * 是否启用代码压缩
	 * @return
	 */
	public boolean isCompress() {
		String value = this.properties.get(COMPRESS).toString(); 
		return Boolean.valueOf(value); 
	}

	
	/**
	 * 获取默认语言
	 * @return
	 */
	public String getDefaultLanguage() {
		return this.properties.getProperty(DEFAULTLANG, "zh-CN");
	}

	
	/**
	 * 是否启用页面静态化
	 * @return
	 */
	public boolean isStaticPage() { 
		String value = this.properties.getProperty(STATIC_PAGE); 
		return Boolean.valueOf(value);  
	}

	
	/**
	 * 获取主页地址
	 * @return
	 */
	public String getHomePage() {
		return this.properties.getProperty(HOME_PAGE);
	}

	
	/**
	 * 是否启用Gzip
	 * @return
	 */
	public boolean isGzip() {
		String value = this.properties.getProperty(GZIP); 
		return Boolean.valueOf(value);
	}

    /**
     * 获取文件存储地址
     * @return
     */
    public String getFilePath() {
        return this.properties.getProperty(FILE_PATH);
    }


	/**
	 * 获取模板配置路径
     * (如果数据库中没有配置则使用当前项目中的themes文件夹)
	 * @return
	 */
	public String getThemesPath(){
        String themesPath = this.properties.getProperty(THEMES_PATH);
        if(StringUtils.isEmpty(themesPath)){
            return WebRealPathHolder.REAL_PATH + "themes";
        }
    	return themesPath;
	}


	/**
	 * 获取当前使用的主题
	 * @return
	 */
	public String getThemeActive(){
		return this.properties.getProperty(THEMES_ACTIVE,"default");
	}


	/**
	 * 获取安全登录码
	 * @return
	 */
	public String getLoginSafe(){
		return this.properties.getProperty(SYSTEM_LOGIN_SAFE,"");
	}

    /**
     * 获取主题缓存目录
     * @return
     */
    public String getThemesCache(){
        return this.properties.getProperty(THEMES_CACHE,"/data/tmp");
    }
}

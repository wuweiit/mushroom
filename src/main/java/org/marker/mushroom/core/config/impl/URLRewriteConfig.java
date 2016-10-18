package org.marker.mushroom.core.config.impl;

import java.io.IOException;

import org.marker.mushroom.core.config.ConfigEngine;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.urlrewrite.URLRewriteEngine;

/**
 * URL重写配置
 * @author marker
 * @date 2013-11-15 下午10:15:12
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class URLRewriteConfig extends ConfigEngine {

	/** 页面后缀key */
	public static final String PAGE_SUFFIX = "page.suffix";
	public static final String URL_CHANNEL = "url.channel";
	public static final String URL_PAGE = "url.page";
	public static final String URL_CONTENT = "url.content";
	
	
	/** 默认页面后缀 */
	public static final String DEFAULT_PAGE_SUFFIX = ".html";
	
	// URL重写配置文件路径
	public static final String CONFIG_FILE_PATH = "/config/urlrewrite/urlrewrite.properties";
	
	
	
	
	/** URL重写 */
	private static final URLRewriteEngine urlRewrite = SingletonProxyFrontURLRewrite.getInstance();

	
	
	/**
	 * 默认构造方法
	 * @throws IOException
	 * */
	private URLRewriteConfig() {
		super(CONFIG_FILE_PATH);
		init();// 初始化URL重写规则到引擎
	}
	
	
	/**
	 * 初始化
	 */
	public void init(){
		String suffix = this.properties.getProperty(PAGE_SUFFIX, DEFAULT_PAGE_SUFFIX);// 页面后缀
		// 加载到URL重写引擎中
		for(Object key : this.properties.keySet()){
			String keyStr = key.toString();
			if(keyStr.startsWith("url.")){
				String rule = this.properties.getProperty(keyStr, "") + suffix;
				urlRewrite.putRule(key.toString(), rule);
				logger.info("rewrite init rule： " + key + "=" + rule);
			}
		}
	}
	
	
 
	
	@Override
	public void set(String key, String rule) {
		super.set(key, rule);
		urlRewrite.putRule(key, rule);
		logger.info("rewrite init rule： " + key + "=" + rule);
	}
	
	
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static URLRewriteConfig instance ;
	
	/**
	 * 获取系统配置实例
	 * */
	public static URLRewriteConfig getInstance(){
		if(null == instance)
			instance = new URLRewriteConfig();
		return instance;
	}
	
	
	@Override
	public void store() {
		super.store();
		this.init();// 初始化URL重写规则到引擎
	}
	
	
 
	/**
	 * 页面后缀(默认: .html)
	 * @return String
	 */
	public String getPageSuffix(){ 
		return this.properties.getProperty(PAGE_SUFFIX, DEFAULT_PAGE_SUFFIX);
	}
 
	 
}

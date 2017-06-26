package org.marker.mushroom.core.config.impl;

import java.io.IOException;

import org.marker.mushroom.core.config.ConfigDBEngine;
import org.marker.mushroom.core.config.ConfigEngine;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.urlrewrite.URLRewriteEngine;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * URL重写配置
 * @author marker
 * @date 2013-11-15 下午10:15:12
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class URLRewriteConfig extends ConfigDBEngine {

	/** 页面后缀key */
	public static final String PAGE_SUFFIX = "page.suffix";
	public static final String URL_CHANNEL = "url.channel";
	public static final String URL_PAGE = "url.page";
	public static final String URL_CONTENT = "url.content";
	
	
	/** 默认页面后缀 */
	public static final String DEFAULT_PAGE_SUFFIX = ".html";

	
	
	
	/** URL重写 */
	private static final URLRewriteEngine urlRewrite = SingletonProxyFrontURLRewrite.getInstance();

	/**
	 * 初始化就读取配置文件哦
	 *
	 * @param jdbcTemplate
	 */
	public URLRewriteConfig(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
		init();// 初始化
	}


    /**
     * 获取实例
     * @return
     */
    public static URLRewriteConfig getInstance() {
        return SpringContextHolder.getBean("URLRewriteConfig");
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

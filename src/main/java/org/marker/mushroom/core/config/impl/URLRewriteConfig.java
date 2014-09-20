package org.marker.mushroom.core.config.impl;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
	private final Log log = LogFactory.getLog(URLRewriteConfig.class);
	
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
		
		// 加载到URL重写引擎中
		for(Object key : this.properties.keySet()){
			String rule = this.properties.getProperty(key.toString());
			urlRewrite.putRule(key.toString(), rule);
			log.info("rewrite init rule： " + key + "=" + rule);
		}
	}
	
	
	
 
	
	@Override
	public void set(String key, String rule) {
		super.set(key, rule);
		urlRewrite.putRule(key, rule);
		log.info("rewrite init rule： " + key + "=" + rule);
	}
	
	
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static class SingletonHolder {
		public final static URLRewriteConfig instance = new URLRewriteConfig();     
	}
	
	
	/**
	 * 获取系统配置实例
	 * */
	public static URLRewriteConfig getInstance(){
		return SingletonHolder.instance;
	}
	
	
 

 
	 
}

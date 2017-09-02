package org.marker.mushroom.core.proxy;

import org.marker.urlrewrite.Parameter;
import org.marker.urlrewrite.URLRewriteEngine;

/**
 * CMS前端URL重写代理
 * 单例代理这样做的目的是为了满足项目中其它重写模式
 *
 *
 * @author marker
 *
 * */
public class SingletonProxyFrontURLRewrite {

	// 需要重写的URL接口地址
	public static final String URL_PATTERN =  "/cms?";

	public final static URLRewriteEngine instance = new URLRewriteEngine(URL_PATTERN);
	static{
		// 利用静态块来初始化一些数据
		// 设置规则参数
		instance.put(new Parameter("p","{channel}","((?!admin)[a-zA-Z_/]+)"));
		instance.put(new Parameter("type","{type}","([a-zA-Z_]+)"));
		instance.put(new Parameter("id","{id}","([0-9]+)"));
		instance.put(new Parameter("time","{time}","([0-9]+)"));
		instance.put(new Parameter("page","{page}","([0-9]+)"));
	}


	
	/**
	 * 获取URLRewriteEngine实例
	 * 虽然获取到了实例，但是还没有添加规则哦
	 * 
	 * 例子：
	 * 	urlRewrite.putRule("channel", "/{channel}.html");
	 *	urlRewrite.putRule("content", "/{channel}/{type}/thread-{id}.html");
	 *	urlRewrite.putRule("page", "/{channel}-{page}.html");
	 *			
	 * @return URLRewriteEngine
	 */
	public static URLRewriteEngine getInstance(){
		return  instance;
	}
	
	 
	
}

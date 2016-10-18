package org.marker.mushroom.core.config.impl;

import java.io.IOException;

import org.marker.mushroom.core.config.ConfigEngine;

/**
 * 错误信息
 * @author marker
 * @date 2013-8-23 下午1:02:29
 * @version 1.0
 */
public class ErrorConfig extends ConfigEngine {
	
	
	public static final String CONFIG_FILE_PATH = "/config/error/error.properties";
	
	
	/**
	 * 默认构造方法
	 * @throws IOException 
	 * */
	private ErrorConfig() {
		super(CONFIG_FILE_PATH);
	}
	
	
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static ErrorConfig instance ;

	
	
	/**
	 * 获取数据库配置实例
	 * */
	public static ErrorConfig getInstance(){
		if(null == instance)
			instance = new ErrorConfig();
		return  instance;
	}
	
	
	
}

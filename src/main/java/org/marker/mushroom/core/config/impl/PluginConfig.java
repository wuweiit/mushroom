package org.marker.mushroom.core.config.impl;

import org.marker.mushroom.core.config.ConfigEngine;



/**
 * 插件配置文件
 * @author marker
 * @version 1.0
 */
public class PluginConfig extends ConfigEngine {
	
	
	public static final String CONFIG_FILE_PATH = "/config/bundle/plugin.properties";
	
	
	
	public PluginConfig() {
		super(CONFIG_FILE_PATH); 
	}
	

	
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static PluginConfig instance;
	
	
	/**
	 * 获取系统配置实例
	 * */
	public static PluginConfig getInstance(){
		if(null == instance)
			instance = new PluginConfig();
		return  instance;
	}
	

}

package org.marker.mushroom.core.config.impl;

import java.io.File;
import java.io.IOException;
 



import org.marker.mushroom.core.config.ConfigEngine;

/**
 * 数据库动态配置
 * 
 * 说明：获取数据库链接信息的途径
 * 实现：继承ConfigEngine实现的
 * 
 * @author marker
 * */
public class DataBaseConfig extends ConfigEngine {

	// 表前缀变量
	public static final String DB_TABLE_PREFIX = "mushroom.db.prefix";
	
	// 配置文件地址
	public static final String CONFIG_FILE_PATH = "/config/jdbc/connection.properties";
	
	
	/**
	 * 默认构造方法
	 * @throws IOException 
	 * */
	private DataBaseConfig() { 
		super(CONFIG_FILE_PATH);
	}

	
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static  DataBaseConfig instance;
	
	
	/**
	 * 获取数据库配置实例
	 * */
	public static synchronized DataBaseConfig getInstance(){
		if(null == instance)
		  instance = new DataBaseConfig();
		return instance;
	}
	
	
	/**
	 * 获取表前缀
	 * */
	public String getPrefix(){
		return  instance.get(DB_TABLE_PREFIX);
		
	}
	
	public interface Names{
		String DESCRIPTION = "desription";
	}


	/**
	 * debug模式
	 * @return  boolean
	 */
	public boolean debug() {
		String debug = (String) this.properties.get("mushroom.db.debug");
		if(debug != null){
			return Boolean.valueOf(debug);
		}
		return false;// 默认情况为false
	}


	
	/**
	 * 是否为debug模式
	 * @return
	 */
	public boolean isDebug() {
		String debug = (String) this.properties.get("mushroom.db.debug");
		if(debug != null){
			return Boolean.valueOf(debug);
		}
		return false;
	}

 
 
	
}

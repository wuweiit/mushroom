package org.marker.mushroom.dao;

import java.util.List;

import org.marker.mushroom.beans.Plugin;

/**
 * 插件数据库操作对象接口
 * @author marker
 * */
public interface IPluginDao {

	
	
	/**
	 * 查询全部的内容模型
	 * @return List<Module> 集合
	 * */
	List<Plugin> queryAll();
	
	
	
	/**
	 * 通过mark查询Plugin
	 * @return Plugin 插件对象
	 * */
	Plugin findByMark(String mark);
	
	
	
	/**
	 * 检查是否安装插件
	 * @param uuid
	 * @return
	 */
	boolean check(String uuid);
	
}

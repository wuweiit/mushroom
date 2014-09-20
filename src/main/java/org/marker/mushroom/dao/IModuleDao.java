package org.marker.mushroom.dao;

import java.util.List;

import org.marker.mushroom.beans.Module;




public interface IModuleDao {

	boolean install(Module module);
	
	
	/**
	 * 检测是否安装
	 * 如果已经安装：返回当前已经安装的对象
	 * 如果没有安装：返回null对象
	 * @param moduleType
	 * @return 
	 */
	Module checkIsInstall(String moduleType);
	
	
	/**
	 * 查询全部的内容模型
	 * @return List<Module> 集合
	 * */
	List<Module> queryAll();


	/**
	 * @param moduleType
	 */
	boolean deleteByType(String moduleType);


	/**
	 * @param moduleType
	 * @return
	 */
	Module findByType(String moduleType);
}

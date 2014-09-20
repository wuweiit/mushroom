package org.marker.mushroom.dao;

import java.util.List;
import java.util.Map;

import org.marker.mushroom.beans.Module;



/**
 * 内容模型
 * @author marker
 * @version 1.0
 */
public interface IModelDao {
 
 
	
	/**
	 * 查询全部的内容模型
	 * @return List<Module> 集合
	 * */
	List<?> queryAll();


	/**
	 * @param moduleType
	 */
	boolean deleteByType(String modelType);


	/**
	 * @param moduleType
	 * @return
	 */
	Module findByType(String modelType);


	boolean save(Map<String, Object> config);
	
	
}

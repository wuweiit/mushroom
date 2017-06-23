package org.marker.mushroom.dao;


import org.marker.mushroom.beans.Channel;

/**
 * 通用Dao接口
 * 
 * <p>
 * 	通用数据库操作接口，提供了基础的数据库操作
 * 
 * 目前功能：
 * </p>
 * @author marker
 * @version 1.0
 */
public interface ICommonDao extends ISupportDao {

	
	/**
	 * 通用批量删除
	 * @param Class<?> clzz 有Entity注解的Bean类
	 * @param String ids 形如："1,3,5,8,2"
	 * @return boolean 处理状态
	 * */
	boolean deleteByIds(Class<?> clzz, String ids);

}

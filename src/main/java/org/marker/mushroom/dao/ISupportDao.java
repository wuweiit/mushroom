package org.marker.mushroom.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Page;


/**
 * 数据库操作支撑接口
 * 
 * @author marker
 * */
public interface ISupportDao {

	
	
	/**
	 * 保存
	 * @param entity 
	 * @return
	 */
	boolean save(Object entity);
	
	
	/**
	 * 更新
	 * @param entity
	 * @return
	 */
	boolean update(Object entity);
	
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public boolean deleteByIds(Class<?> clzz, String ids);
	
	
	/**
	 * 批量删除
	 * @param tableName 表名称
	 * @param key       主键
	 * @param String ids 形如："1,3,5,8,2"
	 * @return boolean 处理状态
	 * */
	boolean deleteByIds(String tableName, String key, String ids);
	
	
	/**
	 * 根据ID查询数据对象
	 * @param Class<?> clzz 有Entity注解的Bean类
	 * @param String ids 形如："1,3,5,8,2"
	 * @param String prefix 表前缀
	 * @return boolean 处理状态
	 * */
	Map<String, Object> findById(Class<?> clzz, Serializable id);
	
	
	public Page findByPage(int currentPageNo, int pageSize, String sql, Object... args);
	

	
	/*
	 * Map<String, Object> 
	 */

	/**
	 * 查询单个对象(Map)
	 * @param sql
	 * @param args
	 * @return Map
	 */
	public Map<String,Object> queryForMap(String sql, Object... args);
	
	public List<Map<String, Object>> queryFotList(int currentPageNo, int pageSize, String sql, Object... args);
	
	public List<Map<String, Object>> queryForList(String sql, Object... args);
	
	public <T> T queryForObject(String sql, Class<T> clzz, Object... args);

    /**
     * 查询是否存在
     * @param sql
     * @param args
     * @return
     */
	public boolean exists(String sql, Object... args);

	/**
	 * 批量更新
	 * @param sql
	 * @param batchArgs
	 * @return
	 */
	public int[] batchUpdate(String sql, List<Object[]> batchArgs);
	
	public boolean update(String sql, Object... args);


	/**
	 * 删除所有数据
	 */
    void deleteAll(Class<?> clzz);

    String getPreFix();


    /**
     * 查询所有数据
     * @return
     */
    Object findAll(Class<?> clzz);
}

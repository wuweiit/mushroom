package org.marker.mushroom.dao;

import java.util.Map;


/**
 * 统计接口
 * @author marker
 * @version 1.0
 */
public interface IStatisticsDao {

	Map<String,Object> today();

	Map<String,Object> yesterday();
	
	
	/**
	 * 根据时间查询该天统计数据
	 * @param time yyyyMMdd
	 * @return
	 */
    Map<String, Object> query(String time);
}

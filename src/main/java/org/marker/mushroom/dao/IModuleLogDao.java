/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.dao;

import java.util.List;

import org.marker.mushroom.beans.ModuleLog;

/**
 * @author marker
 * @date 2013-9-18 上午10:06:20
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public interface IModuleLogDao {

	List<ModuleLog> findByModuleType(String moduleType);
	
	//批量插入日志信息
	int[] batchModuleLog(List<ModuleLog> logs);

	/**
	 * 批量删除日志
	 */

	public boolean deleteModuleLog(String moduleType);
	
}

/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.dao;

import org.marker.mushroom.beans.Page;

/**
 * @author marker
 * @date 2013-11-15 下午10:52:12
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public interface IUserLoginLogDao extends ISupportDao {
	
	
	
	Page queryByPage(int currentPageNo, int pageSize);
	
}

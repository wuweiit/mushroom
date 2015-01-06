/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.dao.impl;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IUserLoginLogDao;
import org.springframework.stereotype.Repository;

/**
 * 用户登录日志Dao
 * @author marker
 * @date 2013-11-15 下午10:51:24
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@Repository(DAO.USER_LOGIN)
public class UserLoginLogDao extends DaoEngine implements IUserLoginLogDao {

	
	
	

	/* (non-Javadoc)
	 * @see org.marker.mushroom.dao.IUserLoginLogDao#queryByPage()
	 */
	@Override
	public Page queryByPage(int currentPageNo,int pageSize) { 
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPreFix()).append("user_login_log order by id desc");
		
		return this.findByPage(currentPageNo, pageSize, sql.toString());
	}
	
	
	
	

}

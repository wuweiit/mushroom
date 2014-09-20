/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.dao.impl;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.UserLoginLog;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IUserLoginLogDao;
import org.marker.qqwryip.IPLocation;
import org.marker.qqwryip.IPTool;
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

	public UserLoginLogDao() {
		super(UserLoginLog.class); 
	}

	// IP归属地获取工具
	private IPTool ipTool = IPTool.getInstance();
	
	 
	public void intsert(String ip, String name, int loginLogType, String info) {
		String prefix = dbConfig.getPrefix(); 
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(prefix).append("user_login_log")
		.append("(username,ip,area,info,time,type) values(?,?,?,?,sysdate(),?)");
		// 通过IP地址查询出地区信息
		String area = "未知地区";
		if(ip != null){
			try{
				IPLocation location = ipTool.getLocation(ip); 
				if(location != null){// 如果存在
					area = location.getCountry();
				}
				this.update(sql.toString(), name, ip, area, info, loginLogType ); 
			}catch(Exception e){
				logger.error("ip={} ",ip, e);
			}
		}
	}

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

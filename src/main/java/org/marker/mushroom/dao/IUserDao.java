package org.marker.mushroom.dao;
 
import java.io.Serializable;
import java.util.List;

import org.marker.mushroom.beans.User;
import org.marker.mushroom.beans.UserGroup;

public interface IUserDao {

	User queryByNameAndPass(String name, String pass);
	
	boolean updateLoginTime(Serializable id);

	
	/**
	 * @param userName
	 * @return
	 */
	User findUserByName(String userName);
	
	/**
	 * 查询所有用户分组
	 * @return
	 */
	List<UserGroup> findGroup();
	
	
	
	/**
	 * 统计用户组的用户数量
	 * @param groupId
	 * @return
	 */
	int countUserByGroupId(int groupId);
}

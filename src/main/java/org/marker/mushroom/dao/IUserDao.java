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


	/**
	 * 更新Token
	 * @param userId
	 * @param token
	 */
	void updateToken(int userId, String token);

	/**
	 * 用户是否存在
	 * @param username
	 * @return
	 */
	boolean existsUserName(String username);

    void save(User user);



    boolean existEmail(String email);

    void updateField(int userId, String field, String value);
}

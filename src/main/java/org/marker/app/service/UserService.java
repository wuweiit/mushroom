package org.marker.app.service;

import org.marker.mushroom.beans.User;

/**
 * Created by ROOT on 2016/12/7.
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return
     */
    boolean existUserName(String username);


    /**
     * 查询用户信息
     * @param username
     * @return
     */
    User findUser(String username);


    /**
     * 更新用户Token
     * @param userId
     */
    String updateUserToken(int userId);


    /**
     * 保存用户
     * @param user
     */
    void save(User user);
}

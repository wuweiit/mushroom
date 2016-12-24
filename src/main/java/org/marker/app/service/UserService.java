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
     * @param sessionId
     */
    String updateUserToken(int userId, String sessionId);


    /**
     * 保存用户
     * @param user
     */
    void save(User user);


    /**
     * 验证邮箱是否存在
     * @param email
     * @return
     */
    boolean existEmail(String email);

    void updateField(int userId, String field, String value);
}

package org.marker.app.business;

import org.marker.app.domain.MessageResult;
import org.marker.mushroom.beans.User;

/**
 *
 *
 *
 * Created by ROOT on 2016/12/7.
 */
public interface UserBusiness {


    /**
     * 注册账号
     * @param username
     * @param password
     * @return
     */
    MessageResult register(String username, String password);


    MessageResult updateField(int userId, String field, String value);

    boolean existUserName(String username);

    User findUser(String username);

    void updateToken(int id, String sessionId);
}

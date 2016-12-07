package org.marker.app.business;

import org.marker.app.domain.MessageResult;

/**
 *
 *
 *
 * Created by ROOT on 2016/12/7.
 */
public interface UserBusiness {


    /**
     * 登录操作
     * @param username
     * @param password
     * @return
     */
    MessageResult login(String username, String password);
}

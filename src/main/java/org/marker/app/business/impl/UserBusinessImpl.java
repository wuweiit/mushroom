package org.marker.app.business.impl;

import org.marker.app.business.UserBusiness;
import org.marker.app.common.ErrorCode;
import org.marker.app.domain.MessageResult;
import org.marker.app.service.UserService;
import org.marker.mushroom.beans.User;
import org.marker.security.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 *
 *
 * Created by ROOT on 2016/12/7.
 */
@Service
public class UserBusinessImpl implements UserBusiness {

    @Autowired
    private UserService userService;

    @Override
    public MessageResult login(String username, String password) {

        // 验证用户是否存在
        if(!userService.existUserName(username)){
            return MessageResult.wrapErrorCode(ErrorCode.USER_NOT_EXISTS);
        }



        // 登录验证(根据用户名和密码查询)
        String loginPasswordMd5 = MD5.getMD5Code(password);

        User user = userService.findUser(username);
        if(user == null || !user.getPass().equals(loginPasswordMd5)){
            return MessageResult.wrapErrorCode(ErrorCode.USER_PASSWORD_ERROR);
        }


        // 登录成功生成Token
        String token = userService.updateUserToken(user.getId());
        user.setToken(token);
        user.setPass(null);

        return MessageResult.success(user);
    }
}

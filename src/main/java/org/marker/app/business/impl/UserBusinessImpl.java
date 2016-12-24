package org.marker.app.business.impl;

import org.marker.app.business.UserBusiness;
import org.marker.app.common.ErrorCode;
import org.marker.app.domain.MessageResult;
import org.marker.app.service.UserService;
import org.marker.mushroom.beans.User;
import org.marker.mushroom.utils.DataUtils;
import org.marker.mushroom.utils.GeneratePass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public MessageResult register(String email, String password) {

        // 验证邮箱格式
        if(!DataUtils.checkEmail(email)){
            return MessageResult.wrapErrorCode(ErrorCode.EMAIL_IS_ERROR);
        }

        // 验证用户是否存在
        if(userService.existEmail(email)){
            return MessageResult.wrapErrorCode(ErrorCode.USER_IS_EXISTS);
        }

        String loginPasswordMd5 = GeneratePass.encode(password);

        User user = new User();
        user.setEmail(email);
        user.setName("");
        user.setNickname("匿名");
        user.setPass(loginPasswordMd5);
        user.setCreatetime(new Date());
        user.setGid(3);// 普通用户
        user.setStatus(0);
        user.setDescription("app 注册");

        userService.save(user);



        return MessageResult.success(user);
    }

    @Override
    public MessageResult updateField(int userId, String field, String value) {

        String allowfields=",";




        userService.updateField(userId, field,value);

        return MessageResult.success();
    }

    @Override
    public boolean existUserName(String username) {
        return userService.existUserName(username);
    }

    @Override
    public User findUser(String username) {
        return userService.findUser(username);
    }

    @Override
    public void updateToken(int userId, String sessionId) {
        userService.updateUserToken(userId, sessionId);
    }
}

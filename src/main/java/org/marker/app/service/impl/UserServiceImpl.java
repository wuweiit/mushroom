package org.marker.app.service.impl;

import org.marker.app.service.UserService;
import org.marker.mushroom.beans.User;
import org.marker.mushroom.dao.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by ROOT on 2016/12/7.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public boolean existUserName(String username) {
        return userDao.existsUserName(username);
    }


    public User findUser(String username) {
        return userDao.findUserByName(username);
    }

    @Override
    public String updateUserToken(int userId, String token) {
//        String token = UUID.randomUUID().toString().replaceAll("-","");
        userDao.updateToken(userId, token);
        return token;
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public boolean existEmail(String email) {
        return userDao.existEmail(email);
    }

    @Override
    public void updateField(int userId, String field, String value) {
        userDao.updateField(  userId, field,value);
    }
}

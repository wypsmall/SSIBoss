package com.neo.user.service.impl;

import com.neo.user.dao.IUserDao;
import com.neo.user.entity.User;
import com.neo.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by neowyp on 2016/3/9.
 */
@Service("userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    protected IUserDao userDao;

    @Override
    public void funcA() {

    }

    @Override
    public void funcB() {

    }

    @Override
    @Transactional
    public void funcC_AB() {
        User tmp = new User();
        tmp.setName("funcC_AB-01-"+System.currentTimeMillis());
        tmp.setPassword("funcC_AB-01");
        userDao.insert(tmp);
        tmp = new User();
        tmp.setName( "12345678901234567890123456789yes-no"); //1234567890
        tmp.setPassword("funcC_AB-02");
        userDao.insert(tmp);
    }
}

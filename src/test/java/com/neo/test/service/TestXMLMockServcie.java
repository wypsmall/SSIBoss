package com.neo.test.service;

import com.neo.common.mock.AopTargetUtils;
import com.neo.user.dao.IUserDao;
import com.neo.user.service.IEmployeeService;
import com.neo.user.service.IOrderService;
import com.neo.user.service.IUserService;
import com.neo.user.service.impl.OrderServiceImpl;
import com.neo.user.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.when;

/**
 * Created by neowyp on 2016/3/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/test-applicationContext*.xml")
public class TestXMLMockServcie {

    @InjectMocks
    UserServiceImpl userService;

    @Autowired
    IUserDao userDao;

    @Before
    public void initMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(AopTargetUtils.getTarget(userService), "userDao", userDao);
    }

    @Test
    public void test_getOrderEmployeeInfo() {
        userService.funcC_AB();
    }

}

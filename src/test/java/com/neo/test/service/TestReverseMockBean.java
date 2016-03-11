package com.neo.test.service;

import com.neo.common.mock.AopTargetUtils;
import com.neo.user.service.IEmployeeService;
import com.neo.user.service.IOrderMockService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.when;

/**
 * Created by neowyp on 2016/3/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext*.xml")
public class TestReverseMockBean {

    @Autowired
    ApplicationContext ctx;

    @Mock
    IEmployeeService employeeService;

    @Autowired
    IOrderMockService orderMockService;



    @Before
    public void initMocks() throws Exception {

        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(AopTargetUtils.getTarget(orderMockService), "employeeService", employeeService);
        when(employeeService.getEmployeeId()).thenReturn("123001");

    }

    @Test
    public void test_getOrderEmployeeInfo() {
        String info = orderMockService.getOrderEmployeeInfo();
        System.out.println("info => " + info);
        Assert.assertEquals(info, "123001");

    }

}

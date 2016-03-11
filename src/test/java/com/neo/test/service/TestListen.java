package com.neo.test.service;

import com.neo.test.BaseTestCase;
import com.neo.user.service.IEmployeeService;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by neowyp on 2016/3/10.
 */
@ContextConfiguration(locations = "classpath*:spring/applicationContext*.xml")
public class TestListen extends BaseTestCase {

    @Mock
    IEmployeeService employeeService;
    @Test
    public void testMethod() {

    }
}

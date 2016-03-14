package com.neo.test.order;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by neowyp on 2016/3/14.
 */
public class TestLoadXML {

    private ApplicationContext ctx;

    @Before
    public void init() {
        ctx = new ClassPathXmlApplicationContext("classpath*:spring/test-applicationContext-mybatis.xml",
                "classpath*:spring/test-orderAppCtx-noMock.xml");
        System.out.println("===");
    }


    @Test
    public void testFunc() {
        System.out.println("============test");
    }
}

package com.neo.test.engine;

import com.neo.engine.chain.HandlerEngine;
import com.neo.engine.event.EventEngine;
import com.neo.engine.event.SimEMOrderCreate;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by neowyp on 2016/3/14.
 */
@Slf4j
public class TestHandlerEngine {

    private ApplicationContext ctx;

    private HandlerEngine handlerEngine;

    @Before
    public void init() {
        ctx = new ClassPathXmlApplicationContext(
                "classpath*:spring/test-HandlerEngineAppCtx.xml"
/*
                , "classpath*:spring/test-applicationContext-mybatis.xml"
*/
        );
        log.info("initialize config completed!");

        String[] strBeans = ctx.getBeanDefinitionNames();
        for (int i = 0; i < strBeans.length; i++) {
            log.info("{}-{}", i, strBeans[i]);
        }

        handlerEngine = (HandlerEngine) ctx.getBean("handlerEngine");
    }


    @Test
    public void testFunc() {
        log.info("============testFunc============");
        handlerEngine.doHandler(new HashMap<String, Object>());
    }


    @After
    public void end() {
        //prevent main thread from exit
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("============end============");
    }

}

package com.neo.test.engine;

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
public class TestEventEngine {

    private ApplicationContext ctx;

    private EventEngine eventEngine;

    @Before
    public void init() {
        ctx = new ClassPathXmlApplicationContext(
                "classpath*:spring/test-EventEngineAppCtx.xml"
/*
                , "classpath*:spring/test-applicationContext-mybatis.xml"
*/
        );
        log.info("initialize config completed!");

        String[] strBeans = ctx.getBeanDefinitionNames();
        for (int i = 0; i < strBeans.length; i++) {
            log.info("{}-{}", i, strBeans[i]);
        }

        eventEngine = (EventEngine) ctx.getBean("eventEngine");
    }


    @Test
    public void testFunc() {
        log.info("============testFunc============");
        SimEMOrderCreate simEMOrderCreate = new SimEMOrderCreate();
        simEMOrderCreate.setBusiNO("0000001");
        simEMOrderCreate.setContext(new HashMap<String, Object>());
        eventEngine.sendMsg(simEMOrderCreate);
    }


    @After
    public void end() {
        //prevent main thread from exit
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("============end============");
    }

}

package com.neo.test.engine;

import com.neo.engine.action.ActionEngine;
import com.neo.engine.action.ActionNode;
import com.neo.engine.action.BusiStatus;
import com.neo.engine.action.OrderDO;
import com.neo.engine.chain.HandlerEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by neowyp on 2016/3/14.
 */
@Slf4j
public class TestActionEngine {

    private ApplicationContext ctx;

    private ActionEngine actionEngine;

    @Before
    public void init() {
        ctx = new ClassPathXmlApplicationContext(
                "classpath*:spring/test-ActionEngineAppCtx.xml"
/*
                , "classpath*:spring/test-applicationContext-mybatis.xml"
*/
        );
        log.info("initialize config completed!");

        String[] strBeans = ctx.getBeanDefinitionNames();
        for (int i = 0; i < strBeans.length; i++) {
            log.info("{}-{}", i, strBeans[i]);
        }

        actionEngine = (ActionEngine) ctx.getBean("actionEngine");
    }


    @Test
    public void testFunc() {
        log.info("============testFunc============");
        List<ActionNode> res = actionEngine.getByGroupId(1);
        for (ActionNode actionNode : res) {
            log.info("an : {}", actionNode);
        }
        res = actionEngine.getByGroupId(1);
        for (ActionNode actionNode : res) {
            log.info("an : {}", actionNode);
        }
    }

    @Test
    public void testStartAction() {
        log.info("============testStartAction============");
        Map<String, Object> context = new HashMap<String, Object>();
        actionEngine.doAction(2, context);
    }

    @Test
    public void testAction() {
        log.info("============testAction============");
        Map<String, Object> context = new HashMap<String, Object>();
        OrderDO order = new OrderDO(1L, 23L, 150L, new Date(), new Date(), BusiStatus.BS_POSTPROCESS.value);
        actionEngine.doAction(order, context);
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

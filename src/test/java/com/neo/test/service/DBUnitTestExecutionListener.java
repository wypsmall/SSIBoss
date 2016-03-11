package com.neo.test.service;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * Created by neowyp on 2016/3/10.
 */
public class DBUnitTestExecutionListener implements TestExecutionListener {
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        System.out.println("beforeTestClass");
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        System.out.println("beforeTestClass");
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        System.out.println("beforeTestMethod");
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        System.out.println("afterTestMethod");
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        System.out.println("afterTestClass");
    }
}

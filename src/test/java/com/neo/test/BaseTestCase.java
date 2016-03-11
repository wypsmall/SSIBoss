package com.neo.test;

import com.neo.common.mock.MockitoDependencyInjectionTestExecutionListener;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by neowyp on 2016/3/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ MockitoDependencyInjectionTestExecutionListener.class })
public abstract class BaseTestCase {

}

package com.neo.test.research.partten1.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by neowyp on 2016/8/29.
 * Author   : wangyunpeng
 * Date     : 2016/8/29
 * Time     : 17:41
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class ValidStore extends ConditionAbs {
    public static AbstractHandler build() {
        return new ValidStore();
    }
    @Override
    public void handler() {
        log.info("ValidStore handler...");
        super.handler();
    }
}

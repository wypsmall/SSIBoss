package com.neo.test.research.partten2.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by neowyp on 2016/9/5.
 * Author   : wangyunpeng
 * Date     : 2016/9/5
 * Time     : 10:14
 * Version  : V1.0
 * Desc     : 返利业务处理handler
 */
@Slf4j
public class RebateHandler extends AbstractHandler {
    public RebateHandler() {
        this.action = "ROLLBACK_REBATE";
    }
    @Override
    public void process() {
        log.info("send rebate mq!");
        super.process();
    }
}

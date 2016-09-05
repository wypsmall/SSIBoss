package com.neo.test.research.partten2.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by neowyp on 2016/9/5.
 * Author   : wangyunpeng
 * Date     : 2016/9/5
 * Time     : 9:59
 * Version  : V1.0
 * Desc     : 帐户业务处理handler
 */
@Slf4j
public class AccountHandler extends AbstractHandler {
    public AccountHandler() {
        this.action = "ROLLBACK_ACCOUNT";
    }
    @Override
    public void process() {
        log.info("use account!");
        super.process();
    }
}

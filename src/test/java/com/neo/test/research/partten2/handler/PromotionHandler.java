package com.neo.test.research.partten2.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by neowyp on 2016/9/5.
 * Author   : wangyunpeng
 * Date     : 2016/9/5
 * Time     : 9:59
 * Version  : V1.0
 * Desc     : 优惠券处理handler
 */
@Slf4j
public class PromotionHandler extends AbstractHandler {
    public PromotionHandler() {
        this.action = "ROLLBACK_PROMOTION";
    }

    @Override
    protected void handler() {
        log.info("use coupon!");
    }

/*    @Override
    public void process() {
        log.info("use coupon!");
        super.process();
    }*/

}

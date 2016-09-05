package com.neo.test.research.partten2.handler;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neowyp on 2016/9/5.
 * Author   : wangyunpeng
 * Date     : 2016/9/5
 * Time     : 10:18
 * Version  : V1.0
 * Desc     : 用于测试的异常handler
 */
@Slf4j
public class ErrorHandler extends AbstractHandler {
    public ErrorHandler() {
        this.action = "ROLLBACK_ERROR";
    }
    @Override
    public void process() {
        throw new HandlerRollBackException();
    }


    public static void main(String[] args) {
        AccountHandler accountHandler = null;
        try {
            List<String> rollBackList = new ArrayList<String>();
            accountHandler = new AccountHandler();
            accountHandler.setRollBackList(rollBackList);

            PromotionHandler promotionHandler = new PromotionHandler();
            promotionHandler.setRollBackList(rollBackList);

            RebateHandler rebateHandler = new RebateHandler();
            rebateHandler.setRollBackList(rollBackList);

            ErrorHandler errorHandler = new ErrorHandler();
            errorHandler.setRollBackList(rollBackList);

            accountHandler.setNextHandler(promotionHandler);
            promotionHandler.setNextHandler(errorHandler);
            errorHandler.setNextHandler(rebateHandler);

            accountHandler.process();
        } catch (Exception e) {
            log.error("test error:", e);
            accountHandler.rollBack();
        }


    }
}

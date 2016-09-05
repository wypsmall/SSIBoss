package com.neo.test.research.partten2.handler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by neowyp on 2016/9/5.
 * Author   : wangyunpeng
 * Date     : 2016/9/5
 * Time     : 9:45
 * Version  : V1.0
 * Desc     : 抽象接口类
 */
@Data
@Slf4j
public abstract class AbstractHandler implements IHandler {
    //下一个待处理的handler
    private AbstractHandler nextHandler;
    //回滚动作列表
    protected List<String> rollBackList;
    //当前handler回归action名称
    protected String action;

    protected abstract void handler();

    @Override
    public void process() {
        try {
            handler();
            if (null != nextHandler) {
                nextHandler.process();
            }
        } catch (HandlerRollBackException e) {
//            log.error("execute error : ", e);
            throw e;
        } finally {
            rollBackList.add(this.action);
        }
    }

    @Override
    public void rollBack() {
        for (String rbAction : rollBackList) {
            log.info("roll back action is [{}]", rbAction);
        }
    }

}

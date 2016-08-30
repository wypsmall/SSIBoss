package com.neo.test.research.partten1.handler;

import com.neo.test.research.partten1.exception.HandlerException;

/**
 * Created by neowyp on 2016/8/29.
 * Author   : wangyunpeng
 * Date     : 2016/8/29
 * Time     : 17:56
 * Version  : V1.0
 * Desc     :
 */
public abstract class AbstractHandler implements IHandler {


    protected AbstractHandler nextHandler;

    public AbstractHandler setNextHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
        return this;
    }

    @Override
    public void handler() {
        if (null != nextHandler)
            nextHandler.handler();
    }

    public static AbstractHandler build() {
        throw new HandlerException();
    }
}

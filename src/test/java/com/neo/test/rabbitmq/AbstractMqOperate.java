package com.neo.test.rabbitmq;

/**
 * Created by neowyp on 2016/7/8.
 * Author   : wangyunpeng
 * Date     : 2016/7/8
 * Time     : 11:12
 * Version  : V1.0
 * Desc     :
 */
public abstract class AbstractMqOperate implements IMqOperate {

    @Override
    public boolean connect() {
        return false;
    }

    @Override
    public boolean sendMessage(String message) {
        return false;
    }

    @Override
    public String receiveMessage() {
        return null;
    }

    @Override
    public boolean closed() {
        return false;
    }
}

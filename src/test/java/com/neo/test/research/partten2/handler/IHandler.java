package com.neo.test.research.partten2.handler;

/**
 * Created by neowyp on 2016/9/5.
 * Author   : wangyunpeng
 * Date     : 2016/9/5
 * Time     : 9:44
 * Version  : V1.0
 * Desc     : 责任链接口类
 */
public interface IHandler {
    /**
     * 处理具体业务
     */
    public void process();

    /**
     * 业务失败回滚时调用
     */
    public void rollBack();
}

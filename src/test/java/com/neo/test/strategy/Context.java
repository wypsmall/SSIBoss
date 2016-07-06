package com.neo.test.strategy;

/**
 * Created by neowyp on 2016/6/17.
 * Author   : wangyunpeng
 * Date     : 2016/6/17
 * Time     : 15:51
 * Version  : V1.0
 * Desc     :
 */
public class Context {
    // 策略对象
    private Strategy strategy;

    // 构造函数
    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    // 策略方法
    public void contextInterface() {
        strategy.strategyInterface();
    }
}

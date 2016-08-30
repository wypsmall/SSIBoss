package com.neo.test.research.partten1.state;

/**
 * Created by neowyp on 2016/8/29.
 * Author   : wangyunpeng
 * Date     : 2016/8/29
 * Time     : 14:31
 * Version  : V1.0
 * Desc     :
 */
public class InitState extends StateAbs {
    @Override
    public StateAbs create() {
        return STATE_WAITINGPAY;
    }
}

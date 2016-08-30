package com.neo.test.research.partten1.state;

/**
 * Created by neowyp on 2016/8/29.
 * Author   : wangyunpeng
 * Date     : 2016/8/29
 * Time     : 14:31
 * Version  : V1.0
 * Desc     :
 */
public class WaitingPayState extends StateAbs {
    @Override
    public StateAbs pay() {
        //发现之前没有编写这个方法
//        return STATE_COMPLETE;
        //2016-08-30
        return STATE_WAITINGCONFIRM;
    }
}

package com.neo.test.research.partten1.state;

/**
 * Created by neowyp on 2016/8/30.
 * Author   : wangyunpeng
 * Date     : 2016/8/30
 * Time     : 10:47
 * Version  : V1.0
 * Desc     :
 */
public class WaitingRefundState extends StateAbs {

    @Override
    public StateAbs refund() {
        return STATE_CLOSE;
    }
}

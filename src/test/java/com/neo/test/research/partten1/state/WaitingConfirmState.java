package com.neo.test.research.partten1.state;

/**
 * Created by neowyp on 2016/8/30.
 * Author   : wangyunpeng
 * Date     : 2016/8/30
 * Time     : 11:49
 * Version  : V1.0
 * Desc     :
 */
public class WaitingConfirmState extends StateAbs {
    @Override
    public StateAbs confirm() {
        return STATE_COMPLETE;
    }
}

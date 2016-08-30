package com.neo.test.research.partten1.state;

/**
 * Created by neowyp on 2016/8/30.
 * Author   : wangyunpeng
 * Date     : 2016/8/30
 * Time     : 10:43
 * Version  : V1.0
 * Desc     :
 */
public class WaitingAuditState extends StateAbs {

    @Override
    public StateAbs disagree() {
        return STATE_COMPLETE;
    }

    @Override
    public StateAbs agree() {
        return STATE_WAITINGREFUND;
    }
}

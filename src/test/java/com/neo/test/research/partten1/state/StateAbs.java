package com.neo.test.research.partten1.state;

import com.neo.test.research.partten1.exception.ForbbidenActionException;

/**
 * Created by neowyp on 2016/8/29.
 * Author   : wangyunpeng
 * Date     : 2016/8/29
 * Time     : 13:56
 * Version  : V1.0
 * Desc     :
 */
public abstract class StateAbs {

    protected static final StateAbs STATE_INIT = new InitState();
    protected static final StateAbs STATE_WAITINGPAY = new WaitingPayState();
    protected static final StateAbs STATE_COMPLETE = new CompleteState();
    protected static final StateAbs STATE_CLOSE = new CloseState();

    public StateAbs create() {
        throw new ForbbidenActionException();
    }
    public StateAbs pay() {
        throw new ForbbidenActionException();
    }
    public StateAbs cancel() {
        throw new ForbbidenActionException();
    }

/*    public void trigger(ConditionAbs condition, ActionAbs action) {
        if(condition.check())
            action.execute();

    }*/
}

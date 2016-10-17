package com.neo.test.research.distributed;

/**
 * Created by neowyp on 2016/10/14.
 * Author   : wangyunpeng
 * Date     : 2016/10/14
 * Time     : 16:18
 * Version  : V1.0
 * Desc     :
 */
@Deprecated
public abstract class BusinessAbs {

    public abstract void commit();
    public abstract void rollback();
}

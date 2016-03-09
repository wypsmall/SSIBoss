package com.neo.user.service;

/**
 * Created by neowyp on 2016/3/9.
 */
public interface IPersonService extends  IUserService {

    public void noTransactionalCfg();
    public void transactionalCfg();
    public void noTransactionalInjectCfg();
}

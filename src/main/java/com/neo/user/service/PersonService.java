package com.neo.user.service;

import com.neo.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.tools.tree.SuperExpression;

/**
 * Created by neowyp on 2016/3/9.
 */
@Service("personService")
public class PersonService extends UserService implements IPersonService {

    @Autowired
    IUserService userService;

    @Override
    public void funcA() {
        super.funcA();
    }

    @Override
    @Transactional
    public void funcB() {
        User tmp = new User();
        tmp.setName("funcB-01-" + System.currentTimeMillis());
        tmp.setPassword("funcB-01");
        userDao.insert(tmp);
        tmp = new User();
        tmp.setName("12345678901234567890123456789yes");//01234567890
        tmp.setPassword("funcB-02");
        userDao.insert(tmp);
    }

    @Override
    public void funcC_AB() {
        super.funcC_AB();
    }

    /*
    funcB();            子类配置事务  事务不生效，没执行一次insert立即插入数据
    super.funcC_AB();   父类配置事务  事务不生效，第一个insert插入，第二个insert异常，没有回滚
    noTransactionalCfg()无事务配置
     */
    @Override
    public void noTransactionalCfg() {
        funcB();
        super.funcC_AB();
    }

    /*
    funcB();            子类配置事务  事务生效，两次insert执行，但依赖整体事务提交
    super.funcC_AB();   父类配置事务  事务生效，第一个insert，第二个insert异常，会使得整体事务回滚一条都插不进去
    TransactionalCfg()  配置事务    整体事务控制在TransactionalCfg配置
     */
    @Override
    @Transactional
    public void transactionalCfg() {
        funcB();
        super.funcC_AB();
    }

    /*
    funcB();            子类配置事务  事务不生效，两次insert执行，但依赖整体事务提交
    userService.funcC_AB();   父类配置事务  事务生效，第一个insert，第二个insert异常，会使当前事务回滚一条都插不进去
    TransactionalCfg()  无事务配置
     */
    @Override
    public void noTransactionalInjectCfg() {
        funcB();
        userService.funcC_AB();
    }
}

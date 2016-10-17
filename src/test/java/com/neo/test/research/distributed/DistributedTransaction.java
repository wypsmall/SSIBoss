package com.neo.test.research.distributed;

import java.lang.annotation.*;

/**
 * Created by neowyp on 2016/10/14.
 * Author   : wangyunpeng
 * Date     : 2016/10/14
 * Time     : 16:33
 * Version  : V1.0
 * Desc     : 分布式事务注解，用于关联逆向方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedTransaction {
    String rbName() default "rollback";
}

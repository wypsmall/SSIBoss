package com.neo.test.research.distributed;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neowyp on 2016/10/14.
 * Author   : wangyunpeng
 * Date     : 2016/10/14
 * Time     : 17:17
 * Version  : V1.0
 * Desc     : 分布式事务拦截层
 */
@Slf4j
public class DisTransAspect {
    /**
     * 使用线程变量记录业务编排信息，当发生异常时，依次回滚之前的方法
     * 后续可以进一步完善，使用eventbus异步回滚
     */
    private static ThreadLocal<Map<Object, String>> threadLocal = new ThreadLocal<Map<Object, String>>() {
        @Override
        public Map<Object, String> initialValue() {
            return new HashMap<Object, String>();
        }
    };

    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        log.info("=========doAround");
        pjp.getTarget();
        Object bean = pjp.getTarget();
        MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
        Method method = joinPointObject.getMethod();

        if (method.isAnnotationPresent(DistributedTransaction.class)) {
            DistributedTransaction dt = method.getAnnotation(DistributedTransaction.class);
            String rbName = dt.rbName();
            threadLocal.get().put(bean, rbName);

        }
        boolean executeRB = false;
        try {
            pjp.proceed();
        } catch (Throwable throwable) {
            log.error("dis trans error : ", throwable);
            executeRB = true;
            throw throwable;
        } finally {
            if (executeRB) {
                Map<Object, String> rbList = threadLocal.get();
                for (Map.Entry<Object, String> entry : rbList.entrySet()) {
                    Object obj = entry.getKey();
                    String rbName = entry.getValue();
                    Method rbMethod = obj.getClass().getMethod(rbName);
                    rbMethod.invoke(obj);
                }
//                log.info("===============rollback method list {}", rbList);
            }
        }


        return null;
    }
}

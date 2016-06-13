package com.neo.test.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import java.util.LinkedHashMap;

/**
 * Created by neowyp on 2016/3/24.
 */
@Slf4j
public class CustomerTest {
    private final static LinkedHashMap<String, Long> cost = new LinkedHashMap<String, Long>();

    public static void main(String[] args) {
        try {
            log.info("");
            cost.put("begin", System.currentTimeMillis());

            KieServices ks = KieServices.Factory.get();
            cost.put("KieServices", System.currentTimeMillis());

            KieContainer kContainer = ks.getKieClasspathContainer();
            cost.put("kContainer", System.currentTimeMillis());

//            KieSession kSession = kContainer.newKieSession("snCustomer");
//            cost.put("KieSession", System.currentTimeMillis());
//
//            int res = kSession.fireAllRules();
//            cost.put("fireAllRules", System.currentTimeMillis());
//            log.info("count {}", res);
//
//            kSession.dispose();
//            cost.put("dispose", System.currentTimeMillis());

            StatelessKieSession lessSession = kContainer.newStatelessKieSession("statelessSnCustomer");
            cost.put("StatelessKieSession", System.currentTimeMillis());

            lessSession.execute(new Object());
            cost.put("execute", System.currentTimeMillis());

            Long before = 0L;
            for (String key : cost.keySet()) {
                Long dt = cost.get(key) - before;
                log.info("{} cost millis is {}", key, dt);
                before = cost.get(key);
            }
        } catch (Exception e) {
            log.error("", e);
        }


    }
}

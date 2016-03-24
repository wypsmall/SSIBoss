package com.neo.test.drools;

import com.neo.drools.Clock;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * Created by neowyp on 2016/3/24.
 */
@Slf4j
public class ClockTest {

    public static void main(String[] args) {
        try {
            log.info("");
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            KieSession kSession = kContainer.newKieSession("session-clock");

            Clock clock = new Clock();
            kSession.insert(clock);
            kSession.fireAllRules();
            log.info("clock {}", clock);
            kSession.dispose();
        } catch (Exception e) {
            log.error("", e);
        }


    }
}

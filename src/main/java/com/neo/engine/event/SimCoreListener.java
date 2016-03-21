package com.neo.engine.event;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by neowyp on 2016/3/21.
 */
@Slf4j
@Component
public class SimCoreListener implements IEvenListener {


    @Subscribe
    public void opOrderCreate(SimEMOrderCreate event) {
        log.info("============opOrderCreate============");
        log.info("params is {}", event);
    }

}

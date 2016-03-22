package com.neo.engine.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by neowyp on 2016/3/22.
 */
@Slf4j
@Component("bsPersistentAction")
public class BSPersistentAction implements IAction {
    @Override
    public void execuate(Map<String, Object> context) {
        log.info("BSPersistentAction param is {}", context);
        context.put("actionNode", "BSPersistentAction");
        log.info("BSPersistentAction set result is {}", context);
    }
}

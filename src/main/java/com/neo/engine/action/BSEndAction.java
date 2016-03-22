package com.neo.engine.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by neowyp on 2016/3/22.
 */
@Slf4j
@Component("bsEndAction")
public class BSEndAction implements IAction {
    @Override
    public void execuate(Map<String, Object> context) {
        log.info("BSStartAction param is {}", context);
        context.put("actionNode", "BSEndAction");
        log.info("BSStartAction set result is {}", context);
    }
}

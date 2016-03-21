package com.neo.engine.chain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by neowyp on 2016/3/21.
 */
@Slf4j
@Component
public class PostProcessorHandler implements IHandler {

    @Override
    public void execuate(Map<String, Object> context) {
        log.info("PostProcessorHandler param is {}", context);
    }
}

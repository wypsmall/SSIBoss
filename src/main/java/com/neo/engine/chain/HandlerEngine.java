package com.neo.engine.chain;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by neowyp on 2016/3/21.
 */
@Slf4j
@Component
public class HandlerEngine {

    @Autowired
    private List<IHandler> handlerChains;


    public void doHandler(Map<String, Object> context) {
        for (IHandler handler : handlerChains) {
            handler.execuate(context);
        }
    }
}

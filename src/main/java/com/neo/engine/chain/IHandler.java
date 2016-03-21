package com.neo.engine.chain;

import java.util.Map;

/**
 * Created by neowyp on 2016/3/21.
 */
public interface IHandler {
    public void execuate(Map<String, Object> context);
}

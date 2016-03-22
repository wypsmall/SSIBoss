package com.neo.engine.action;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neowyp on 2016/3/22.
 */
public class ConstantData {

    public static Map<String, Integer> statusNode = new HashMap<String, Integer>();

    {
        statusNode.put(BusiStatus.BS_START.value, 1);
        statusNode.put(BusiStatus.BS_PREPROCESS.value, 2);
        statusNode.put(BusiStatus.BS_PERSISTENT.value, 3);
        statusNode.put(BusiStatus.BS_POSTPROCESS.value, 4);
        statusNode.put(BusiStatus.BS_END.value, 5);
    }
}

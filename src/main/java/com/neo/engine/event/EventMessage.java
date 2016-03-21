package com.neo.engine.event;

import lombok.Data;

import java.util.Map;

/**
 * Created by neowyp on 2016/3/21.
 */
@Data
public class EventMessage {
    private String busiNO;
    private Map<String, Object> context;
}

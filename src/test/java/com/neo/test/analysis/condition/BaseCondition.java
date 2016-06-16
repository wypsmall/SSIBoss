package com.neo.test.analysis.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neowyp on 2016/6/13.
 * Author   : wangyunpeng
 * Date     : 2016/6/13
 * Time     : 11:28
 * Version  : V1.0
 * Desc     :
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BaseCondition implements Serializable {
    private static final long serialVersionUID = -8771937761515769276L;

    private Map<String, Object> params = new HashMap<String, Object>();

    public Object getParam(String key) {
        return  params.get(key);
    }

    public Object setParam(String key, Object value) {
        return params.put(key, value);
    }
}

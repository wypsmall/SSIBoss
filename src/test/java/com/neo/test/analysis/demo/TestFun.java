package com.neo.test.analysis.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neowyp on 2016/6/12.
 * Author   : wangyunpeng
 * Date     : 2016/6/12
 * Time     : 16:58
 * Version  : V1.0
 * Desc     :
 */
public class TestFun {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        String s = map.put("key", "001");
        System.out.println(s);

        s = map.put("key", "001");
        System.out.println(s);

        s = map.put("key", "002");
        System.out.println(s);

        s = map.put("key", "003");
        System.out.println(s);
    }
}

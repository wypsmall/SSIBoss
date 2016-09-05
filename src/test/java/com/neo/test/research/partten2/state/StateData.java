package com.neo.test.research.partten2.state;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by neowyp on 2016/9/2.
 * Author   : wangyunpeng
 * Date     : 2016/9/2
 * Time     : 14:44
 * Version  : V1.0
 * Desc     : 封装数据存储的实体，可以通过数据库表机型配置
 * 增加了version控制
 * todo 单测没有对version进行单测
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateData {
    private String nodeFrom;
    private String edge;
    private String nodeTo;

    private String version;

    public StateData(String nodeFrom, String edge, String nodeTo) {
        this.nodeFrom = nodeFrom;
        this.edge = edge;
        this.nodeTo = nodeTo;
    }
}

package com.neo.test.research.partten2;

import lombok.Data;

/**
 * Created by neowyp on 2016/9/2.
 * Author   : wangyunpeng
 * Date     : 2016/9/2
 * Time     : 14:33
 * Version  : V1.0
 * Desc     :
 */
@Data
public class Edge {
    private String name;
    private Vertex nodeFrom;
    private Vertex nodeTo;

}

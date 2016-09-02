package com.neo.test.research.partten2;

import lombok.Data;

/**
 * Created by neowyp on 2016/9/2.
 * Author   : wangyunpeng
 * Date     : 2016/9/2
 * Time     : 14:33
 * Version  : V1.0
 * Desc     : 状态机路径抽象，edge可以翻译成【边】
 * 主要的属性是起始和终结节点，name表示的动作名称，也可以理解成触发器trigger
 * 由多个edge就可以构建出状态机流转，实际就是个有向图
 */
@Data
public class Edge {
    private String name;
    private Vertex nodeFrom;
    private Vertex nodeTo;

}

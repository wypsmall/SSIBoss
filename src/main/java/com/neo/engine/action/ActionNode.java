package com.neo.engine.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Created by neowyp on 2016/3/22.
 */
@Data
@ToString
@AllArgsConstructor
public class ActionNode {
    private Integer id;//当前节点ID
    private Integer pid;//父节点
    private Integer nid;//next节点
    private Integer groudid;//分组id
    private String actionBean;//action bean名称
}

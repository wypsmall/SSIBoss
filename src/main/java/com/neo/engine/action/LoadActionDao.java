package com.neo.engine.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by neowyp on 2016/3/22.
 */
@Slf4j
@Component("loadActionDao")
public class LoadActionDao {
    @Autowired
    private List<ActionNode> actionNodes;


    public List<ActionNode> getGoupById(Integer groupid) {
        List<ActionNode> res = new ArrayList<ActionNode>();
        for(ActionNode actionNode : actionNodes) {
            if (actionNode.getGroudid().equals(groupid))
                res.add(actionNode);
        }
        return res;
    }

    public ActionNode getById(Integer id) {
        for(ActionNode actionNode : actionNodes) {
            if (actionNode.getId().equals(id))
                return actionNode;
        }
        return null;
    }

    public static void main(String[] args) {
        /*
        bsStartAction
        bsPreProcessAction
        bsPersistentAction
        bsPostProcessAction
        bsEndAction
         */

        ActionNode actionNode = new ActionNode(1, 1, 2, 1, "bsStartAction");
        log.info("actionNode => {}", actionNode);
    }
}

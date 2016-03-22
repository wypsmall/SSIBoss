package com.neo.engine.action;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import sun.rmi.server.LoaderHandler;

import java.util.List;
import java.util.Map;

/**
 * Created by neowyp on 2016/3/22.
 */
@Slf4j
@Component
public class ActionEngine {

    @Autowired
    private LoadActionDao loadActionDao;

    @Autowired
    ApplicationContext ctx;

    private LoadingCache<Integer, List<ActionNode>> groupActionCatch;
    private LoadingCache<Integer, ActionNode> actionCatch;
//    private LoadingCache<String, Integer> statusActionCatch;

    public ActionEngine() {
        groupActionCatch = CacheBuilder.newBuilder().build(new CacheLoader<Integer, List<ActionNode>>() {
            @Override
            public List<ActionNode> load(Integer key) throws Exception {
                log.info("loading groupid is {}", key);
                return loadActionDao.getGoupById(key);
            }
        });

        actionCatch = CacheBuilder.newBuilder().build(new CacheLoader<Integer, ActionNode>() {
            @Override
            public ActionNode load(Integer key) throws Exception {
                log.info("loading id is {}", key);
                return loadActionDao.getById(key);
            }
        });
    }

    public List<ActionNode> getByGroupId(Integer groupid) {
        return groupActionCatch.getUnchecked(groupid);
    }

    public void doAction(Integer id, Map<String, Object> context) {
        ActionNode actionNode = actionCatch.getUnchecked(id);
        IAction action = (IAction) ctx.getBean(actionNode.getActionBean());
        action.execuate(context);
    }

    public void doAction(OrderDO orderDO, Map<String, Object> context) {
        Integer nodeId = ConstantData.statusNode.get(orderDO.getStatus());
        context.put("orderInfo", orderDO);
        doAction(nodeId, context);
    }
}

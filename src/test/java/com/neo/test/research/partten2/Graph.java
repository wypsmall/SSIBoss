package com.neo.test.research.partten2;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neowyp on 2016/9/2.
 * Author   : wangyunpeng
 * Date     : 2016/9/2
 * Time     : 14:35
 * Version  : V1.0
 * Desc     :
 * 鉴于partten1中遇到的问题，如果发现节点变更，而不是新增节点，源节点状态对应的类需要修改，这样无法扩展
 * partten2提出一种图论的解决方式，思路如下
 * 状态视作vertex，触发状态流转的动作视为edge，状态机用graph表示，同时指定version
 * edge内聚了vertex，而vertex不依赖edge
 * 同时秉承partten1中的思想，graph中不会内聚context，接触了依赖
 */
@Slf4j
public class Graph {


    public static final String STATE_INIT = "init";
    public static final String STATE_WAITING_PAY = "waitingPay";
    public static final String STATE_WAITING_CONFIRM = "waitingConfirm";
    public static final String STATE_COMPLETE = "complete";
    public static final String STATE_CLOSE = "close";
    public static final String STATE_WAITING_AUDIT = "waitingAudit";
    public static final String STATE_WAITING_REFUND = "waitingRefund";


    public static final String TRIGGER_CREATE = "create";//创建订单
    public static final String TRIGGER_PAY = "pay";//支付订单
    public static final String TRIGGER_CONFIRM = "confirm";//确认支付完成
    public static final String TRIGGER_CANCEL = "cancel";//未支付取消
    public static final String TRIGGER_APPLY_CANCEL = "applyCancel";//支付完成申请取消
    public static final String TRIGGER_DISAGREE = "disagress";//不同意取消
    public static final String TRIGGER_AGREE = "agree";//同意取消
    public static final String TRIGGER_REFUND = "refund";//退款


    private List<Edge> edges;
    private String version;

    public Graph constructGraph(StateData[] states, String version) {

        edges = new ArrayList<Edge>();
        this.version = version;
        for (StateData state : states) {
            Edge edge = new Edge();
            edge.setName(state.getEdge());
            edge.setNodeFrom(new Vertex(state.getNodeFrom()));
            edge.setNodeTo(new Vertex(state.getNodeTo()));
            edges.add(edge);
        }
        return this;
    }

    public Vertex trigger(String curVertexName, String edge) {
        for (Edge curEdge : edges) {
            if (curEdge.getNodeFrom().getName().equals(curVertexName)) {
                if (curEdge.getName().equals(edge))
                    return curEdge.getNodeTo();
            }
        }
        throw new ForbbidenException();
    }

    public static void main(String[] args) {
        /*
        init        create          waitingpay
        waitingpay  pay             complete
        waitingpay  cancel          close
         */
        String ver = "1.0";
        StateData create = new StateData(STATE_INIT, TRIGGER_CREATE, STATE_WAITING_PAY);
        StateData pay = new StateData(STATE_WAITING_PAY, TRIGGER_PAY, STATE_COMPLETE);
        StateData cancel = new StateData(STATE_WAITING_PAY, TRIGGER_CANCEL, STATE_CLOSE);
        Graph graph = new Graph();
        graph.constructGraph(new StateData[]{create, pay, cancel}, ver);

        String curState = STATE_COMPLETE;
        String operation = TRIGGER_APPLY_CANCEL;
        Vertex vertex = null;
        try {
            vertex = graph.trigger(curState, operation);
        } catch (Exception e) {
            log.error("forbbiden operation {}-{}-{}", curState, operation, ver, e);
        }

        log.info("Graph {}-{}-{}, next vertex is [{}]", curState, operation, ver, vertex);

        curState = STATE_WAITING_PAY;
        operation = TRIGGER_PAY;
        try {
            vertex = graph.trigger(curState, operation);
        } catch (Exception e) {
            log.error("forbbiden operation {}-{}-{}", curState, operation, ver, e);
        }

        log.info("Graph {}-{}-{}, next vertex is [{}]", curState, operation, ver, vertex);


        ver = "1.2";
        pay = new StateData(STATE_WAITING_PAY, TRIGGER_PAY, STATE_WAITING_CONFIRM);
        StateData confirm = new StateData(STATE_WAITING_CONFIRM, TRIGGER_CONFIRM, STATE_COMPLETE);

        graph = new Graph();
        graph.constructGraph(new StateData[]{create, pay, cancel, confirm}, ver);
        curState = STATE_WAITING_PAY;
        operation = TRIGGER_PAY;
        try {
            vertex = graph.trigger(curState, operation);
        } catch (Exception e) {
            log.error("forbbiden operation {}-{}-{}", curState, operation, ver, e);
        }

        log.info("Graph {}-{}-{}, next vertex is [{}]", curState, operation, ver, vertex);

        ver = "2.0";
        StateData applyCancel = new StateData(STATE_COMPLETE, TRIGGER_APPLY_CANCEL, STATE_WAITING_AUDIT);
        StateData disagree = new StateData(STATE_WAITING_AUDIT, TRIGGER_DISAGREE, STATE_COMPLETE);
        StateData agree = new StateData(STATE_WAITING_AUDIT, TRIGGER_AGREE, STATE_WAITING_REFUND);
        StateData refund = new StateData(STATE_WAITING_REFUND, TRIGGER_REFUND, STATE_CLOSE);

        graph = new Graph();
        graph.constructGraph(new StateData[]{create, pay, cancel, applyCancel, disagree, agree, refund}, ver);
        curState = STATE_COMPLETE;
        operation = TRIGGER_APPLY_CANCEL;
        try {
            vertex = graph.trigger(curState, operation);
        } catch (Exception e) {
            log.error("forbbiden operation {}-{}-{}", curState, operation, ver, e);
        }

        log.info("Graph {}-{}-{}, next vertex is [{}]", curState, operation, ver, vertex);
    }
}

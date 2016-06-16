package com.neo.test.analysis.demo;

/**
 * Created by neowyp on 2016/6/15.
 * Author   : wangyunpeng
 * Date     : 2016/6/15
 * Time     : 11:24
 * Version  : V1.0
 * Desc     :
 * http://wangkuiwu.github.io/2013/04/09/basic-list-dg-java/
 */
public class ListDGStr {
    // 邻接表中表对应的链表的顶点
    private class ENode {
        int ivex;       // 该边所指向的顶点的位置
        ENode nextEdge; // 指向下一条弧的指针
    }

    // 邻接表中表的顶点
    private class VNode {
        String data;          // 顶点信息
        ENode firstEdge;    // 指向第一条依附该顶点的弧
    }

    ;

    private VNode[] mVexs;  // 顶点数组


    /*
     * 创建图(用已提供的矩阵)
     *
     * 参数说明：
     *     vexs  -- 顶点数组
     *     edges -- 边数组
     */
    public ListDGStr(String[] vexs, String[][] edges) {

        // 初始化"顶点数"和"边数"
        int vlen = vexs.length;
        int elen = edges.length;

        // 初始化"顶点"
        mVexs = new VNode[vlen];
        for (int i = 0; i < mVexs.length; i++) {
            mVexs[i] = new VNode();
            mVexs[i].data = vexs[i];
            mVexs[i].firstEdge = null;
        }

        // 初始化"边"
        for (int i = 0; i < elen; i++) {
            // 读取边的起始顶点和结束顶点
            String c1 = edges[i][0];
            String c2 = edges[i][1];
            // 读取边的起始顶点和结束顶点
            int p1 = getPosition(edges[i][0]);
            int p2 = getPosition(edges[i][1]);

            // 初始化node1
            ENode node1 = new ENode();
            node1.ivex = p2;
            // 将node1链接到"p1所在链表的末尾"
            if (mVexs[p1].firstEdge == null)
                mVexs[p1].firstEdge = node1;
            else
                linkLast(mVexs[p1].firstEdge, node1);
        }
    }

    /*
     * 将node节点链接到list的最后
     */
    private void linkLast(ENode list, ENode node) {
        ENode p = list;

        while (p.nextEdge != null)
            p = p.nextEdge;
        p.nextEdge = node;
    }

    /*
     * 返回ch位置
     */
    private int getPosition(String ch) {
        for (int i = 0; i < mVexs.length; i++)
            if (mVexs[i].data.equals(ch))
                return i;
        return -1;
    }

    /*
     * 打印矩阵队列图
     */
    public void print() {
        System.out.printf("List Graph:\n");
        for (int i = 0; i < mVexs.length; i++) {
//            System.out.printf("%d(%s): ", i, mVexs[i].data);
            System.out.printf("[%s]: ", mVexs[i].data);
            ENode node = mVexs[i].firstEdge;
            while (node != null) {
//                System.out.printf("%d(%s) ", node.ivex, mVexs[node.ivex].data);
                System.out.printf("[%s] ", mVexs[node.ivex].data);
                node = node.nextEdge;
            }
            System.out.printf("\n");
        }
    }

    public static void main(String[] args) {
        String[] vexs = {"A", "B", "C", "D", "E", "F", "G"};
        String[][] edges = new String[][]{
                {"A", "B"},
                {"B", "C"},
                {"B", "E"},
                {"B", "F"},
                {"C", "E"},
                {"D", "C"},
                {"E", "B"},
                {"E", "D"},
                {"F", "G"}};
        ListDGStr pG;

        // 自定义"图"(输入矩阵队列)
        //pG = new ListDG();
        // 采用已有的"图"
        pG = new ListDGStr(vexs, edges);

        pG.print();   // 打印图
    }
}

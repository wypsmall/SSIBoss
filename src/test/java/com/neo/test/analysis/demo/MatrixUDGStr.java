package com.neo.test.analysis.demo;

/**
 * Created by neowyp on 2016/6/14.
 * Author   : wangyunpeng
 * Date     : 2016/6/14
 * Time     : 18:11
 * Version  : V1.0
 * Desc     :
 */
import java.util.Scanner;

public class MatrixUDGStr {

    private String[] mVexs;       // 顶点集合
    private int[][] mMatrix;    // 邻接矩阵

    /*
     * 创建图(用已提供的矩阵)
     *
     * 参数说明：
     *     vexs  -- 顶点数组
     *     edges -- 边数组
     */
    public MatrixUDGStr(String[] vexs, String[][] edges) {

        // 初始化"顶点数"和"边数"
        int vlen = vexs.length;
        int elen = edges.length;

        // 初始化"顶点"
        mVexs = new String[vlen];
        for (int i = 0; i < mVexs.length; i++)
            mVexs[i] = vexs[i];

        // 初始化"边"
        mMatrix = new int[vlen][vlen];
        for (int i = 0; i < elen; i++) {
            // 读取边的起始顶点和结束顶点
            int p1 = getPosition(edges[i][0]);
            int p2 = getPosition(edges[i][1]);

            mMatrix[p1][p2] = 1;
            mMatrix[p2][p1] = 1;
        }
    }

    /*
     * 返回ch位置
     */
    private int getPosition(String ch) {
        for(int i=0; i<mVexs.length; i++)
            if(mVexs[i]==ch)
                return i;
        return -1;
    }


    /*
     * 读取一个输入字符
     */
    private int readInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    /*
     * 打印矩阵队列图
     */
    public void print() {
        System.out.printf("Martix Graph:\n");
        for (int i = 0; i < mVexs.length; i++) {
            for (int j = 0; j < mVexs.length; j++)
                System.out.printf("%d ", mMatrix[i][j]);
            System.out.printf("\n");
        }
    }

    public static void main(String[] args) {
        String[] vexs = {"A", "B", "C", "D", "E", "F", "G"};
        String[][] edges = new String[][]{
                {"A", "C"},
                {"A", "D"},
                {"A", "F"},
                {"B", "C"},
                {"C", "D"},
                {"E", "G"},
                {"F", "G"}};
        MatrixUDGStr pG;

        // 自定义"图"(输入矩阵队列)
        //pG = new MatrixUDG();
        // 采用已有的"图"
        pG = new MatrixUDGStr(vexs, edges);

        pG.print();   // 打印图
    }
}
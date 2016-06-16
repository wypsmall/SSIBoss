package com.neo.test.analysis.demo;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

/**
 * Created by neowyp on 2016/6/15.
 * Author   : wangyunpeng
 * Date     : 2016/6/15
 * Time     : 17:13
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class Rotate {
    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().add(new RotatePanel());
        jf.setPreferredSize(new Dimension(500, 400));
        jf.pack();
        jf.setVisible(true);
    }
}

@Slf4j
class RotatePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        String s = "Java";
        Font f = new Font("宋体", Font.BOLD, 12);
        Color[] colors = {Color.black, Color.LIGHT_GRAY};
        g2d.setFont(f);
//        //   平移原点到图形环境的中心
//        g2d.translate(this.getWidth() / 2, this.getHeight() / 2);
//        //   旋转文本
//        for (int i = 0; i < 12; i++) {
//            g2d.rotate(30 * Math.PI / 180);
//            g2d.setPaint(colors[i % 2]);
//            g2d.drawString(s, 0, 0);
//        }

//        g2d.translate(this.getWidth() / 2, this.getHeight() / 2);
//        g2d.setColor(colors[0]);
//        g2d.fillOval(-5,-5,10,10);

//        g2d.rotate(270 * Math.PI / 180);
//        g2d.setPaint(colors[1]);
//        g2d.drawString(s, 50, 50);
        g2d.setPaint(colors[0]);
        int agv = 6, r = 100;

        g2d.fillOval(150 - 5, 150 - 5, 10, 10);
        Point[] ps = new Point[agv];
        for (int i = 0; i < agv; i++) {
            double rad = 360 * i / agv * Math.PI / 180; //计算弧度
            Point point = new Point();
            point.x = (int) (Math.cos(rad) * r) + 150;
            point.y = (int) (Math.sin(rad) * r) + 150;
            ps[i] = point;
            log.info("{},{}", point, rad);
            g2d.rotate(rad, ps[i].x, ps[i].y);
            g2d.drawString("你好-"+i, ps[i].x, ps[i].y);
            g2d.rotate(rad*-1, ps[i].x, ps[i].y);
        }
        /*
0.0,java.awt.Point[x=50,y=100]
1.0471975511965976,java.awt.Point[x=93,y=75]
2.0943951023931953,java.awt.Point[x=93,y=26]
3.141592653589793,java.awt.Point[x=50,y=0]
4.1887902047863905,java.awt.Point[x=7,y=25]
5.235987755982989,java.awt.Point[x=7,y=75]
         */

        g2d.drawString("你好-0", ps[0].x, ps[0].y);

        g2d.rotate(1.0471975511965976, ps[1].x, ps[1].y);
        g2d.drawString("你好-1", ps[1].x, ps[1].y);
        g2d.rotate(-1.0471975511965976, ps[1].x, ps[1].y);

        g2d.rotate(2.0943951023931953, ps[2].x, ps[2].y);
        g2d.drawString("你好-2", ps[2].x, ps[2].y);
        g2d.rotate(-2.0943951023931953, ps[2].x, ps[2].y);

        g2d.rotate(3.141592653589793, ps[3].x, ps[3].y);
        g2d.drawString("你好-2", ps[3].x, ps[3].y);

//        for (int i = 0; i < agv; i++) {
//            g2d.rotate(360/agv * Math.PI / 180);
//            double rad = 360 * i / agv * Math.PI / 180; //计算弧度
//            Point point = new Point();
//            point.x = (int) (Math.sin(rad) * r);
//            point.y = (int) (Math.cos(rad) * r);
//            g2d.drawString(""+i+"--"+s+"---"+i+"("+point.x+"," + point.y+")", point.x, point.y);
//            log.info("[{},{}],[{},{}]", 360 * i / agv, 360 * i / agv, Math.sin(rad), Math.cos(rad));
//        }
    }
}
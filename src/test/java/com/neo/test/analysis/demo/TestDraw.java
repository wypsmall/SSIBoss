package com.neo.test.analysis.demo;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by neowyp on 2016/6/15.
 * Author   : wangyunpeng
 * Date     : 2016/6/15
 * Time     : 13:57
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class TestDraw {

    public static void main(String[] args) {
        drawPic();
//        calPoint();
    }

    private static List<Point> calPoint(int agv) {
        List<Point> points = new ArrayList<>();
        int R = 400;
        Point cet = new Point(600, 450);
        for (int i = 0; i < agv; i++) {
            double rad = 360 * i / agv * Math.PI / 180;
            Point point = new Point();
            point.x = cet.x + (int) (Math.sin(rad) * R);
            point.y = cet.y + (int) (Math.cos(rad) * R);
            points.add(point);
            log.info("[{},{}],[{},{}]", 360 * i / agv, 360 * i / agv, Math.sin(rad), Math.cos(rad));
        }
        log.info("{}", points);
        return points;
    }

    private static List<Point> calPointByParam(int agv, int x, int y, int r) {
        List<Point> points = new ArrayList<>();
        Point cet = new Point(x, y);
        for (int i = 0; i < agv; i++) {
            double rad = 360 * i / agv * Math.PI / 180; //计算弧度
            Point point = new Point();
            point.x = cet.x + (int) (Math.cos(rad) * r);
            point.y = cet.y + (int) (Math.sin(rad) * r);
            points.add(point);
//            log.info("[{},{}],[{},{}]", 360 * i / agv, 360 * i / agv, Math.sin(rad), Math.cos(rad));
        }
        log.info("{}", points);
        return points;
    }
    private static void paintComponent(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        String s = "Java 2d 旋转";
        Font f = new Font("宋体", Font.BOLD, 16);
        Color[] colors = { Color.ORANGE, Color.LIGHT_GRAY };
        g2d.setFont(f);

        //   平移原点到图形环境的中心
        g2d.translate(width / 2, height / 2);

        //   旋转文本
        for (int i = 0; i < 12; i++) {
            g2d.rotate(30 * Math.PI / 180);
            g2d.setPaint(colors[i % 2]);
            g2d.drawString(s, 0, 0);
        }
    }
    private static void drawPic() {
        try {
            int width = 1200;
            int height = 900;
            // 创建BufferedImage对象
            Font font = new Font("宋体", Font.PLAIN, 12);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 获取Graphics2D
            Graphics2D g2d = image.createGraphics();
            // 画图
            g2d.setBackground(new Color(255, 255, 255));
            g2d.setPaint(new Color(0, 0, 0));
            g2d.clearRect(0, 0, width, height);
            g2d.drawString("名称：娃哈哈纯净水", 0, 10);
            g2d.drawString("产地：浙江杭州", 0, 26);
            g2d.drawString("品牌：娃娃哈哈", 0, 42);
            g2d.drawString("单价：9876543210", 0, 58);
            g2d.setFont(font);

//            paintComponent(g2d, width, height);

            int agvs = 80;
            List<Point> points = calPointByParam(agvs, width / 2, height / 2, 400);
            List<Point> labPoints = calPointByParam(agvs, width/2, height/2, 395);
            for (int i = 0; i < points.size(); i++) {
                double rad = 360 * i / points.size() * Math.PI / 180; //计算弧度

                g2d.rotate(rad, points.get(i).x, points.get(i).y);
                g2d.drawString("你好---" + i, points.get(i).x, points.get(i).y);
                g2d.rotate(rad*-1, points.get(i).x, points.get(i).y);
            }

            g2d.drawLine(labPoints.get(3).x, labPoints.get(3).y, labPoints.get(27).x, labPoints.get(27).y);


            g2d.drawOval(1, 1, 60, 60);
            //释放对象
            g2d.dispose();
            // 保存文件
            File file = new File("test.png");
            log.info("{}", file.getAbsoluteFile());
            ImageIO.write(image, "png", file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

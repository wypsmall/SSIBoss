package com.neo.test.analysis.scan;

import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.demo.ListDGStr;
import com.neo.test.analysis.demo.ListUDGStr;
import com.neo.test.analysis.process.IProcessScan;
import com.neo.test.analysis.process.ProcessPomByDom4j;
import com.neo.test.analysis.result.BaseResult;
import com.neo.test.analysis.result.ProDependency;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by neowyp on 2016/6/14.
 * Author   : wangyunpeng
 * Date     : 2016/6/14
 * Time     : 9:59
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class FastScanPomFile extends AbstractScanFile {

    public FastScanPomFile(IProcessScan processScan) {
        super(processScan);
    }

    @Override
    public List<BaseResult> scanByCdt(File path, BaseCondition condition) {
        String[] exs = (String[]) condition.getParam("excludes");
        String[] ins = (String[]) condition.getParam("includes");

        List<BaseResult> results = new ArrayList<BaseResult>();
        File[] fs = path.listFiles();
        for (int i = 0; i < fs.length; i++) {

            if (null != exs) {
                boolean flag = false;
                for (int j = 0; j < exs.length; j++) {
                    if (fs[i].getAbsolutePath().contains(exs[j])) {
//                        log.info("exs=>{}", fs[i].getAbsoluteFile());
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    continue;
            }
            if (null != ins) {
                boolean flag = false;
                for (int k = 0; k < ins.length; k++) {
                    if (fs[i].getAbsolutePath().contains(ins[k])) {
//                        log.info("ins=>{}", fs[i].getAbsoluteFile());
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                    continue;
            }

            if (fs[i].isFile()) {
                List<BaseResult> fileScanRes = processScan.process(fs[i], condition);
                if (null != fileScanRes)
                    results.addAll(fileScanRes);
            }

            if (fs[i].isDirectory()) {
                try {
                    List<BaseResult> dirScanRes = scanByCdt(fs[i], condition);
                    if (null != dirScanRes)
                        results.addAll(dirScanRes);
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
        return results;


    }

    public static void main(String[] args) {
        try {
            String filePath = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\";
            ProcessPomByDom4j pomProcess = new ProcessPomByDom4j();
            FastScanPomFile scan = new FastScanPomFile(pomProcess);

            scan.tagPoint("begin");
            BaseCondition condition = new BaseCondition();
            String[] ins = new String[]{"venus-", "trunk\\"};
            String[] exs = new String[]{"tags\\",};
            condition.setParam("includes", ins);
            condition.setParam("excludes", exs);
            condition.setParam("FILENAME_REGEX", "pom.xml");
            List<BaseResult> rts = scan.scanByCdt(new File(filePath), condition);
            scan.tagPoint("scan");
            int count = 0;
            for (BaseResult rt : rts) {
                log.info("--------------------");
                ProDependency dependency = (ProDependency) rt;
                log.info("{}|{}-{}.{}[{}]", count, dependency.getArtifactId(), dependency.getVersion(), dependency.getPackaging(), dependency.getFileName());
                if (dependency.getChildren() != null) {
                    int chcount = 0;
                    for (ProDependency child : dependency.getChildren()) {
                        log.info("   {}|{}-{}.{}", chcount, child.getArtifactId(), child.getVersion(), child.getPackaging());
                        chcount++;
                    }
                }
                count++;
            }
            scan.tagPoint("print");

            List<String[]> pclist = analysisDependency(rts);
            scan.tagPoint("ayalysis");
//            constructTree(pclist);
//            scan.tagPoint("construct");
            scan.printCost();

        } catch (Exception e) {
            log.error("", e);
        }
    }

    private static List<String[]> analysisDependency(List<BaseResult> rts) {
        log.info("===================================");
        Map<String, Map<String, String>> res = new HashMap<>();
        List<String[]> depy = new ArrayList<>();
        Set<String> vexs = new HashSet<>();

        for (BaseResult rt : rts) {
            ProDependency dependency = (ProDependency) rt;
            vexs.add(dependency.getArtifactId());
            if (dependency.getChildren() != null) {
                for (ProDependency child : dependency.getChildren()) {
                    if (!child.getArtifactId().contains("venus"))
                        continue;
                    String key = dependency.getArtifactId() + "->" + child.getArtifactId();
                    vexs.add(child.getArtifactId());
                    if (!res.containsKey(key)) {
//                        log.info("--{}--",key);
                        Map<String, String> dep = new HashMap<String, String>();
                        dep.put(dependency.getArtifactId(), child.getArtifactId());
                        res.put(key, dep);
                        depy.add(new String[]{dependency.getArtifactId(), child.getArtifactId()});
                    }
                }
            }
        }

        log.info("[{}],{}", res.size(), res.keySet());
        log.info("[{}],{}", vexs.size(), vexs);
        String[][] edges = new String[depy.size()][];
        for (int i = 0; i < depy.size(); i++) {
            edges[i] = depy.get(i);
        }
        String[] a = new String[]{};
        String[] svexs = (String[]) vexs.toArray(a);

        ListUDGStr pG;

        // 自定义"图"(输入矩阵队列)
        //pG = new ListUDG();
        // 采用已有的"图"
        pG = new ListUDGStr(svexs, edges);

        pG.print();   // 打印图

        log.info("===================================");

        ListDGStr pGS;
        pGS = new ListDGStr(svexs, edges);
        pGS.print();

        paintFile(svexs, edges);
        return depy;
    }

    private static void paintFile(String[] vexs, String[][] edges) {
        try {
            int width = 2000;
            int height = 1500;
            // 创建BufferedImage对象
            Font font = new Font("宋体", Font.PLAIN, 18);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 获取Graphics2D
            Graphics2D g2d = image.createGraphics();
            // 画图
            g2d.setBackground(new Color(255, 255, 255));
            g2d.setPaint(new Color(0, 0, 0));
            g2d.clearRect(0, 0, width, height);

            g2d.setFont(font);

            int agvs = vexs.length;
            List<Point> points = calPointByParam(agvs, width / 2, height / 2, 400);
            List<Point> labPoints = calPointByParam(agvs, width / 2, height / 2, 395);
            for (int i = 0; i < points.size(); i++) {
                double rad = 360 * i / points.size() * Math.PI / 180; //计算弧度

                g2d.rotate(rad, points.get(i).x, points.get(i).y);
                g2d.drawString(vexs[i], points.get(i).x, points.get(i).y);
                g2d.rotate(rad * -1, points.get(i).x, points.get(i).y);
            }
            for (int i = 0; i < edges.length; i++) {
                String from = edges[i][0];
                String to = edges[i][1];
                int x=0,y=0;
                for (int j = 0; j < vexs.length; j++) {
                    if(from.equals(vexs[j]))
                        x=j;
                    if(to.equals(vexs[j]))
                        y=j;

                }
                g2d.drawLine(labPoints.get(x).x, labPoints.get(x).y, labPoints.get(y).x, labPoints.get(y).y);
            }
            log.info("edges.length:{}", edges.length);

            //释放对象
            g2d.dispose();
            // 保存文件
            File file = new File("test.png");
            log.info("{}", file.getAbsoluteFile());
            ImageIO.write(image, "png", file);
        } catch (Exception ex) {
            log.error("", ex);
        }

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
    private static void constructTree(List<String[]> depy) {
        log.info("===================================");
        for (String[] node : depy) {

        }
    }

}
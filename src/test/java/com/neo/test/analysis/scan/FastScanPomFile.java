package com.neo.test.analysis.scan;

import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.demo.topsort.TopsortDG;
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
 * 1.扫描trunk目录下pom.xml文件通过dom4j解析
 * 2.分析工程之间的依赖关系
 */
@Slf4j
public class FastScanPomFile extends AbstractScanFile {

    public FastScanPomFile(IProcessScan processScan) {
        super(processScan);
    }

    /**
     * 优化AbstractScanFile基类，增加
     * <br/>excludes 互斥条件
     * <br/>includes 包含条件
     *
     * @param file      待处理的文件/文件夹对象
     * @param condition 扫描条件，或约束条件
     * @return 扫描
     */
    @Override
    public List<BaseResult> scanByCdt(File file, BaseCondition condition) {
        //互斥条件
        String[] exs = (String[]) condition.getParam("excludes");
        //包含条件
        String[] ins = (String[]) condition.getParam("includes");

        List<BaseResult> results = new ArrayList<BaseResult>();
        File[] fs = file.listFiles();
        for (int i = 0; i < fs.length; i++) {

            //循环检查互斥条件，当发现有一个满足，就不再检查其他互斥条件break
            //然后continue，处理下一个文件
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
            //循环检查包含条件，多个条件是“与”（&&），
            //然后continue，处理下一个文件
            //TODO 这段代码逻辑有问题，需要优化2016-06-17记录
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
//            log.info("match file:{}", fs[i].getAbsoluteFile());
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

    /**
     * 验证pom依赖分析结果
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            //扫描代码路径
            String filePath = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\";
            //使用DOM4j解析pom文件
            ProcessPomByDom4j pomProcess = new ProcessPomByDom4j();
            //将处理类设置给文件扫描类
            FastScanPomFile scan = new FastScanPomFile(pomProcess);

            //记录锚点
            scan.tagPoint("begin");
            //设置搜索条件
            BaseCondition condition = new BaseCondition();
            String[] ins = new String[]{"venus-", "trunk\\"};
            String[] exs = new String[]{"tags\\",};
            condition.setParam("includes", ins);//包含
            condition.setParam("excludes", exs);//排除
            condition.setParam("FILENAME_REGEX", "pom.xml");//TODO 后期应该改成正则
            /**
             * 结果集是以每个工程维度记录所依赖工程
             * A[B,C,D]
             * B[C,E]
             */
            List<BaseResult> rts = scan.scanByCdt(new File(filePath), condition);
            //记录扫描锚点
            scan.tagPoint("scan");
            //日志输出记录集
            printDependRes(rts);
            scan.tagPoint("print");

            /**
             * objectMap.put("edges", edges);  edges String[][]
             * objectMap.put("vexs", svexs);   svexs String[]
             * objectMap.put("depy", depy);    depy  List<String[]>
             */
            Map<String, Object> objectMap = analysisDependency(rts);
            scan.tagPoint("ayalysis");

            String[][] edges = (String[][]) objectMap.get("edges");
            String[] vexs = (String[]) objectMap.get("vexs");
            List<String[]> depy = (List<String[]>) objectMap.get("depy");

            log.info("===================================");

            TopsortDG topsortDG = new TopsortDG(vexs, edges);
            int res = topsortDG.topologicalSort();

            if (res == 0)
                log.info("sort success!");
            scan.tagPoint("topologicalSort");

            //TODO test
/*            String[] proNames = new String[]{"venus-trade-api"};
            Map<String, Object> resMap = analysisBeDepByProName(depy, proNames);
            for (Map.Entry<String, Object> entry : resMap.entrySet()) {
                log.info("==========analysisBeDepByProName:{}=============",entry.getKey());
                List<String> proDeps = (List<String>)entry.getValue();
                for (String proDep : proDeps) {
                    log.info("child:{}", proDep);
                }

            }*/

/*            String[] proNames = new String[]{"venus-trade"};
            Map<String, Object> resMap = analysisByProName(depy, proNames);
            for (Map.Entry<String, Object> entry : resMap.entrySet()) {
                log.info("==========analysisByProName:{}=============",entry.getKey());
                List<String> proDeps = (List<String>)entry.getValue();
                for (String proDep : proDeps) {
                    log.info("child:{}", proDep);
                }

            }*/

            List<String[]> findRes = findBeDepEdgs(depy, "venus-user-api");
            String[][] fEdges = getEdges(findRes);
            String[] fVexs = getVexs(findRes);

            paintFile(fVexs, fEdges);
            scan.tagPoint("paintFile");

            checkApiDep(depy);

            log.info("===================================");

//            ListDGStr pGS;
//            pGS = new ListDGStr(vexs, edges);
//            pGS.print();
//
//            scan.tagPoint("DGStr");

            log.info("===================================");

//            paintFile(vexs, edges);
//            scan.tagPoint("paintFile");

            scan.printCost();

        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 日志输出记录集
     *
     * @param rts
     */
    private static void printDependRes(List<BaseResult> rts) {
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
    }

    /**
     * 分析依赖关系，并将其展平成String[]数组，长度为2
     * String[0]  String[1]
     * A    ->   B        表示A依赖B
     *
     * @param rts 依赖关系
     * @return
     */
    private static Map<String, Object> analysisDependency(List<BaseResult> rts) {
        log.info("==================================");
        Map<String, Object> objectMap = new HashMap<String, Object>();
        /**
         * A->B，A依赖B，数据存储格式
         *  res     key        value
         *         "A->B"       map<key, value>
         *                           A     B
         */
        Map<String, Map<String, String>> res = new HashMap<>();
        /**
         * String[0]  String[1]
         *    A    ->   B        表示A依赖B
         */
        List<String[]> depy = new ArrayList<>();

        //记录所有工程名字
        Set<String> vexs = new HashSet<>();

        for (BaseResult rt : rts) {
            ProDependency dependency = (ProDependency) rt;
            vexs.add(dependency.getArtifactId());
            if (dependency.getChildren() != null) {
                for (ProDependency child : dependency.getChildren()) {
                    //TODO 目前只分析venus开头的工程，之后需要分析第三方jar的依赖
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

        String[][] edges = getEdges(depy);
        String[] svexs = getVexs(vexs);

        objectMap.put("edges", edges);
        objectMap.put("vexs", svexs);
        objectMap.put("depy", depy);

        return objectMap;
    }


    //TODO 根据输入工程分析依赖工程，开发ing，问题：没有滤重

    /**
     * 分析依赖关系
     *
     * @param depy
     * @param pubNames
     * @return
     */
    private static Map<String, Object> analysisByProName(List<String[]> depy, String[] pubNames) {
        Map<String, Object> objectMap = new HashMap<String, Object>();

        for (int i = 0; i < pubNames.length; i++) {
            List<String> proDeps = findDependency(depy, pubNames[i]);
            objectMap.put(pubNames[i], proDeps);
        }

        return objectMap;
    }

    /**
     * 递归查询依赖，包括间接依赖  TODO 没有滤重
     *
     * @param depy
     * @param proName
     * @return
     */
    private static List<String> findDependency(List<String[]> depy, String proName) {
        List<String> deps = new ArrayList<String>();
        String child = null;
        for (int i = 0; i < depy.size(); i++) {
            if (proName.equals(depy.get(i)[0])) {
                //添加依赖
                deps.add(depy.get(i)[1]);
                List<String> children = findDependency(depy, depy.get(i)[1]);
                deps.addAll(children);
            }
        }
        return deps;
    }

    /**
     * 查询依赖此工程的模块
     *
     * @param depy
     * @param pubNames
     * @return
     */
    private static Map<String, Object> analysisBeDepByProName(List<String[]> depy, String[] pubNames) {
        Map<String, Object> objectMap = new HashMap<String, Object>();

        for (int i = 0; i < pubNames.length; i++) {
            List<String> proDeps = findBeDependency(depy, pubNames[i]);
            objectMap.put(pubNames[i], proDeps);
        }

        return objectMap;
    }

    /**
     * 递归依赖此工程的模块，包括间接依赖  TODO 没有滤重
     *
     * @param depy
     * @param proName
     * @return
     */
    private static List<String> findBeDependency(List<String[]> depy, String proName) {
        List<String> deps = new ArrayList<String>();
        String child = null;
        for (int i = 0; i < depy.size(); i++) {
            if (proName.equals(depy.get(i)[1])) {
                //添加依赖
                deps.add(depy.get(i)[0]);
                List<String> children = findBeDependency(depy, depy.get(i)[0]);
                deps.addAll(children);
            }
        }
        return deps;
    }

    /**
     * 查找直接依赖
     *
     * @param depy
     * @param proName
     * @return
     */
    private static List<String[]> findBeDepEdgs(List<String[]> depy, String proName) {
        List<String[]> deps = new ArrayList<String[]>();
        String child = null;
        for (int i = 0; i < depy.size(); i++) {
            if (proName.equals(depy.get(i)[1])) {
                //添加依赖
                deps.add(depy.get(i));
//                List<String[]> children = findBeDepEdgs(depy, depy.get(i)[0]);
//                deps.addAll(children);
            }
        }
        return deps;
    }

    /**
     * 检查API依赖API
     *
     * @param depy
     * @return
     */
    private static List<String[]> checkApiDep(List<String[]> depy) {
        log.info("+++++++++++++++++++++++++");
        List<String[]> deps = new ArrayList<String[]>();
        for (int i = 0; i < depy.size(); i++) {
            if (depy.get(i)[0].contains("-api") && depy.get(i)[1].contains("-api")) {
                deps.add(depy.get(i));
                log.info("{}->{}", depy.get(i)[0], depy.get(i)[1]);
            }
        }
        log.info("+++++++++++++++++++++++++");
        return deps;
    }

    private static String[] getVexs(Set<String> vexs) {
        String[] a = new String[]{};
        return (String[]) vexs.toArray(a);
    }

    private static String[][] getEdges(List<String[]> depy) {
        String[][] edges = new String[depy.size()][];
        for (int i = 0; i < depy.size(); i++) {
            edges[i] = depy.get(i);
        }
        return edges;
    }

    private static String[] getVexs(List<String[]> depy) {
        Set<String> vexs = new HashSet<String>();
        for (int i = 0; i < depy.size(); i++) {
            vexs.add(depy.get(i)[0]);
            vexs.add(depy.get(i)[1]);
        }
        return getVexs(vexs);
    }


    /**
     * 将节点和依赖关系打印成原型图并连线，有向图
     *
     * @param vexs
     * @param edges
     */
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
                int x = 0, y = 0;
                for (int j = 0; j < vexs.length; j++) {
                    if (from.equals(vexs[j]))
                        x = j;
                    if (to.equals(vexs[j]))
                        y = j;

                }
                int pr = 4;
                g2d.drawOval(labPoints.get(x).x - pr, labPoints.get(x).y - pr, pr * 2, pr * 2);
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


}
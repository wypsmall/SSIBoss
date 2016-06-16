package com.neo.test.analysis.demo;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by neowyp on 2016/6/12.
 * Author   : wangyunpeng
 * Date     : 2016/6/12
 * Time     : 16:58
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class TestFun {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        String s = map.put("key", "001");
        System.out.println(s);

        s = map.put("key", "001");
        System.out.println(s);

        s = map.put("key", "002");
        System.out.println(s);

        s = map.put("key", "003");
        System.out.println(s);
        String jarName = "venus-trade-${venus-trade.version}.jar";
        String regex = "";
        Pattern p = Pattern.compile("\\$\\{(.*)\\}");
        Matcher m = p.matcher(jarName);
        while (m.find()) {
            System.out.println(jarName.substring(m.start(), m.end()));
        }

        List<String[]> depy = new ArrayList<>();
        depy.add(new String[]{"begin", "end"});
        depy.add(new String[]{"begin1", "end1"});
        String[][] edges = new String[depy.size()][];
        for (int i = 0; i < depy.size(); i++) {
            edges[i] = depy.get(i);
        }
        for (int i = 0; i < edges.length; i++) {

            log.info("{}", edges[i]);
        }

        Set<String> nodes = new HashSet<String>();
        nodes.add("fda");
        String[] a = new String[]{};
        String[] v = (String[]) nodes.toArray(a);
        log.info("{}", v);

        try {
            String path = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\venus-common\\trunk\\pom.xml";
            File pomFile = new File(path);
            SAXReader sr = new SAXReader();
            Document doc  =  sr.read(pomFile);
            Element root = doc.getRootElement();
            log.info("{}", root.getName());

            // 当前节点下面子节点迭代器
            Iterator<Element> it = root.elementIterator();
            // 遍历
            while (it.hasNext()) {
                // 获取某个子节点对象
                Element e = it.next();
                // 对子节点进行遍历
                log.info("{}", e.getName());
            }
            log.info("artifactId:{}", root.element("artifactId").getText());
//            log.info("groupId:{}", root.element("groupId").getName());
            log.info("version:{}", root.element("version").getName());
            log.info("packaging:{}", root.element("packaging").getName());
            log.info("dependencies:{}", root.element("dependencies").getName());
        } catch (Exception e) {
            log.error("", e);
        }
    }
}

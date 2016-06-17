package com.neo.test.analysis.process;

import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.result.BaseResult;
import com.neo.test.analysis.result.ProDependency;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by neowyp on 2016/6/15.
 * Author   : wangyunpeng
 * Date     : 2016/6/15
 * Time     : 10:03
 * Version  : V1.0
 * Desc     : 继承IProcessScan，重载process方法，通过dom4j解析pom文件
 */
@Slf4j
public class ProcessPomByDom4j implements IProcessScan {
    @Override
    public List<BaseResult> process(File file, BaseCondition condition) {
        //调试是可以打开
//        log.info("file:{}, condition:{}", file.getAbsoluteFile(), condition);
        List<BaseResult> rts = new ArrayList<BaseResult>();
        try {
            if (null == file)//对象为空返回null
                return rts;
            if (!file.isFile())//不是文件对象返回null
                return rts;
            //TODO 扫描路径中包含"venus-"，则个限制已经提取到FastScanPomFile中，梢后删除
            if (!file.getAbsolutePath().contains("venus-"))
                return rts;
            //TODO 扫描路径中包含"trunk"，则个限制已经提取到FastScanPomFile中，梢后删除
            if (!file.getAbsolutePath().contains("trunk"))
                return rts;

            //TODO 文件名匹配规则，稍后应该修改成正则匹配
            String matchesStr = (String) condition.getParam("FILENAME_REGEX");
            if (!file.getName().equals(matchesStr)) { // "pom.xml"
                return rts;
            }
            log.info("file:{}, condition:{}", file.getAbsoluteFile(), condition);
            //初始化Document对象
            SAXReader sr = new SAXReader();
            Document doc = sr.read(file);
            Element root = doc.getRootElement();

            if (root != null && "project".equals(root.getName())) {
                ProDependency self = constructByElement(root);
                self.setFileName(file.getAbsoluteFile().toString());

                Element element = root.element("dependencies");
                if (null != element) {
                    Iterator<Element> it = element.elementIterator();
                    // 遍历
                    while (it.hasNext()) {
                        // 获取某个子节点对象
                        Element che = it.next();
                        // 对子节点进行遍历
                        ProDependency child = constructByElement(che);
//                        log.info("{}", che.getName());

                        self.addChild(child);
                    }
                }
                rts.add(self);
            }

        } catch (Exception e) {
            log.error("file:{}", file, e);
        }
        return rts;
    }

    /**
     * 分析子节点，组装ProDependency对象，因为project节点、dependencies的子节点都包含
     * artifactId、groupId、version、packaging
     * @param parent 父节点Element对象
     * @return ProDependency对象
     */

    private ProDependency constructByElement(Element parent) {
        ProDependency dep = new ProDependency();
        Element element = parent.element("artifactId");
        if (null != element)
            dep.setArtifactId(element.getText());
        element = parent.element("groupId");
        if (null != element)
            dep.setGroupId(element.getText());
        element = parent.element("version");
        if (null != element)
            dep.setVersion(element.getText());
        element = parent.element("packaging");
        if (null != element)
            dep.setPackaging(element.getText());
        return dep;
    }

    /**
     * 开发测试
     * @param args
     */
    public static void main(String[] args) {
        try {
            String path = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\venus-common\\trunk\\pom.xml";
            ProcessPomByDom4j process = new ProcessPomByDom4j();
            BaseCondition condition = new BaseCondition();

//            String[] ins = new String[]{"venus-", "trunk\\"};
//            String[] exs = new String[]{"tags\\",};
//            condition.setParam("includes", ins);
//            condition.setParam("excludes", exs);
            condition.setParam("FILENAME_REGEX", "pom.xml");

            List<BaseResult> rts = process.process(new File(path), condition);

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
        } catch (Exception e) {
            log.error("", e);
        }
    }
}

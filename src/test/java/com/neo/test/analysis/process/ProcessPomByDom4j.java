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
 * Desc     :
 */
@Slf4j
public class ProcessPomByDom4j implements IProcessScan {
    @Override
    public List<BaseResult> process(File file, BaseCondition condition) {
        //        log.info("file:{}, condition:{}", file.getAbsoluteFile(), condition);
        List<BaseResult> rts = new ArrayList<BaseResult>();
        try {
            if (null == file)
                return rts;
            if (!file.isFile())
                return rts;
            if (!file.getAbsolutePath().contains("venus-"))
                return rts;
            if (!file.getAbsolutePath().contains("trunk"))
                return rts;
            String matchesStr = (String) condition.getParam("FILENAME_REGEX");
            if (!file.getName().equals(matchesStr)) { // "pom.xml"
                return rts;
            }

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

    private ProDependency constructByElement(Element root) {
        ProDependency dep = new ProDependency();
        Element element = root.element("artifactId");
        if (null != element)
            dep.setArtifactId(element.getText());
        element = root.element("groupId");
        if (null != element)
            dep.setGroupId(element.getText());
        element = root.element("version");
        if (null != element)
            dep.setVersion(element.getText());
        element = root.element("packaging");
        if (null != element)
            dep.setPackaging(element.getText());
        return dep;
    }

    public static void main(String[] args) {
        try {
            String path = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\venus-common\\trunk\\pom.xml";
            ProcessPomByDom4j process = new ProcessPomByDom4j();
            BaseCondition condition = new BaseCondition();
            String[] ins = new String[]{"venus-", "trunk\\"};
            String[] exs = new String[]{"tags\\",};
            condition.setParam("includes", ins);
            condition.setParam("excludes", exs);
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

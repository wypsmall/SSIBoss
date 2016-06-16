package com.neo.test.analysis.process;

import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.result.BaseResult;
import com.neo.test.analysis.result.ProDependency;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neowyp on 2016/6/13.
 * Author   : wangyunpeng
 * Date     : 2016/6/13
 * Time     : 12:17
 * Version  : V1.0
 * Desc     : 作废，SAX没有找到处理深层嵌套出现的同样的name的情况，改用DOM4j处理
 */
@Slf4j
@Deprecated
public class ProcessPomDepend extends DefaultHandler implements IProcessScan {
    @Getter
    private List<BaseResult> resultList = new ArrayList<BaseResult>();
    @Setter
    private BaseCondition condition;

    private String currentNode = "";

    private ProDependency self = null;
    private ProDependency tmpdep = null;
    private String node;
    @Setter
    private String fileName;

    @Override
    public void startDocument() throws SAXException {
        resultList = new ArrayList<BaseResult>();
        self = new ProDependency();

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        log.info("startElement => uri:[{}], localName:[{}], qName:[{}], attributes:[{}]", uri, localName, qName, attributes);
//        if( "dubbo:reference".equals(qName) ) {

        if ("project".equals(qName)) {
            currentNode = "project";
        } else if ("dependency".equals(qName)) {
            currentNode = "dependency";
            tmpdep = new ProDependency();
        }

        node = qName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        ProDependency cur = null;
        if ("project".equals(currentNode)) {
            cur = self;
        } else if ("dependency".equals(currentNode)) {
            cur = tmpdep;
        }
        if (null != cur) {
            String content = new String(ch, start, length);
//            log.info("{},[{}]", node, content);
            if (node.equals("groupId")) {
//                if (cur.getGroupId() == null)
                cur.setGroupId(content);
            } else if (node.equals("artifactId")) {
//                if (cur.getArtifactId() == null)
                cur.setArtifactId(content);
            } else if (node.equals("version")) {
//                if (cur.getVersion() == null)
                cur.setVersion(content);
            } else if (node.equals("packaging")) {
//                if (cur.getPackaging() == null)
                cur.setPackaging(content);
            }

        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("project".equals(qName)) {
            self.setFileName(fileName);
            resultList.add(self);
        }
        if ("dependency".equals(qName)) {
            self.addChild(tmpdep);
        }

        if ("dependencies".equals(qName)) {
            currentNode = null;
        }

        node = "";
    }

    @Override
    public List<BaseResult> process(File file, BaseCondition condition) {
//        log.info("file:{}, condition:{}", file.getAbsoluteFile(), condition);
        List<BaseResult> rts = null;
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

            fileName = file.getAbsoluteFile().toString();
            log.info("file:{}, condition:{}", file.getAbsoluteFile(), condition);
//            String filePath = file.getAbsolutePath();
            InputStream inputStream = new FileInputStream(file);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ProcessPomDepend handler = new ProcessPomDepend();
            handler.setFileName(fileName);
            handler.setCondition(condition);
            parser.parse(inputStream, handler);

            rts = handler.getResultList();
        } catch (Exception e) {
            log.error("file:{}", file, e);
        }
        return rts;
    }

    public static void main(String[] args) {
        try {
            String path = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\venus-common\\trunk\\pom.xml";
            ProcessPomDepend process = new ProcessPomDepend();
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

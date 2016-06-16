package com.neo.test.analysis.process;

import com.neo.test.analysis.ServiceInfo;
import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.result.BaseResult;
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
 * Desc     :
 */
@Slf4j
public class ProcessDubboProvider extends DefaultHandler implements IProcessScan {
    @Getter
    private List<BaseResult> resultList = new ArrayList<BaseResult>();
    @Setter
    private BaseCondition condition;

    @Override
    public void startDocument() throws SAXException {
        resultList = new ArrayList<BaseResult>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        log.info("startElement => uri:[{}], localName:[{}], qName:[{}], attributes:[{}]", uri, localName, qName, attributes);
//        if( "dubbo:reference".equals(qName) ) {
        if (condition.getParam("dubbo").equals(qName)) {
            int len = attributes.getLength();
            for (int i = 0; i < len; i++) {
//                log.info("localName:[{}], value:[{}]", attributes.getLocalName(i), attributes.getValue(i));
            }
            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setBeanId(attributes.getValue("ref"));
            serviceInfo.setVersion(attributes.getValue("version"));
            serviceInfo.setInfName(attributes.getValue("interface"));
            resultList.add(serviceInfo);
        }
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
            String matchesStr = (String) condition.getParam("FILENAME_REGEX");
            if (!file.getName().matches(matchesStr)) { // ".*\\.(?i)xml"
                return rts;
            }
            log.info("file:{}, condition:{}", file.getAbsoluteFile(), condition);
//            String filePath = file.getAbsolutePath();
            InputStream inputStream = new FileInputStream(file);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ProcessDubboProvider handler = new ProcessDubboProvider();
            handler.setCondition(condition);
            parser.parse(inputStream, handler);

            rts = handler.getResultList();
        } catch (Exception e) {
            log.error("file:{}", file, e);
        }
        return rts;
    }
}

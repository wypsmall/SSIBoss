package com.neo.test.analysis.demo;

import com.neo.test.analysis.ServiceInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neowyp on 2016/6/12.
 * Author   : wangyunpeng
 * Date     : 2016/6/12
 * Time     : 11:49
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class AnalysisXMLForDubbo extends DefaultHandler {
    @Getter
    private List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();

    @Override
    public void startDocument() throws SAXException {
        serviceInfos = new ArrayList<ServiceInfo>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        log.info("startElement => uri:[{}], localName:[{}], qName:[{}], attributes:[{}]", uri, localName, qName, attributes);
        if( "dubbo:reference".equals(qName) ) {
            int len = attributes.getLength();
            for (int i = 0; i < len; i++) {
//                log.info("localName:[{}], value:[{}]", attributes.getLocalName(i), attributes.getValue(i));
            }
            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setBeanId(attributes.getValue("id"));
            serviceInfo.setVersion(attributes.getValue("version"));
            serviceInfo.setInfName(attributes.getValue("interface"));
            serviceInfos.add(serviceInfo);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        log.info("endElement => uri:[{}], localName:[{}], qName:[{}]", uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
//        log.info("characters =>  start:[{}], length:[{}]",  start, length);
    }

    public static void main(String[] args) {
        InputStream inputStream = null;
        try {
            String filePath = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\venus-pay\\trunk\\src\\main\\resources\\spring\\venus-pay-dubbo-consumer.xml";
            inputStream = new FileInputStream(filePath);
            AnalysisXMLForDubbo analysisXML = new AnalysisXMLForDubbo();
            List<ServiceInfo> datas = analysisXML.parserXml(inputStream);
            log.info("{}", datas);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<ServiceInfo> parserXml(InputStream xmlStream) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            AnalysisXMLForDubbo handler = new AnalysisXMLForDubbo();
            parser.parse(xmlStream, handler);
//            log.info("serviceInfos:{}", handler.getServiceInfos());
            return handler.getServiceInfos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

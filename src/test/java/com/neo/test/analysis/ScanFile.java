package com.neo.test.analysis;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by neowyp on 2016/6/12.
 * Author   : wangyunpeng
 * Date     : 2016/6/12
 * Time     : 13:48
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class ScanFile {


    public List<ScanResult> scanParseFile(File root, List<ServiceInfo> serviceInfos) {
        List<ScanResult> scanRes = new ArrayList<ScanResult>();
        for (int i = 0; i < serviceInfos.size(); i++) {
            List<ScanResult> tscanRes = scanDir(root, serviceInfos.get(i));
            scanRes.addAll(tscanRes);
        }
        return scanRes;
    }

    public List<ScanResult> scanDir(File dir, ServiceInfo serviceInfo) {
        List<ScanResult> scanRes = new ArrayList<ScanResult>();
        File[] fs = dir.listFiles();
        for (int i = 0; i < fs.length; i++) {
            String path = fs[i].getAbsolutePath();
            if (path.matches(".*\\.(?i)java")) {
                //扫描文件
//                System.out.println(path);
                List<ScanResult> fileScanRes = parseFile(fs[i], serviceInfo);
                scanRes.addAll(fileScanRes);
            }
            if (fs[i].isDirectory()) {
                try {
                    List<ScanResult> dirScanRes = scanDir(fs[i], serviceInfo);
                    scanRes.addAll(dirScanRes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return scanRes;
    }

    private List<ScanResult> parseFile(File file, ServiceInfo serviceInfo) {
        List<ScanResult> res = new ArrayList<ScanResult>();
        try {
            FileReader reader = new FileReader(file);
//            BufferedReader br = new BufferedReader(reader);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String str = null;
            int line = 0;
            while ((str = br.readLine()) != null) {
                line++;
                if (str.contains(serviceInfo.getBeanId() + ".")) {
                    String lstr = str;
//                    String lstr = new String(str.getBytes("GBK"),"UTF-8");
//                    System.out.println(file.getName() + "-" + line + "-" + lstr);
                    ScanResult scanResult = parseLine(lstr, serviceInfo.getBeanId());
                    scanResult.setLineNum(line);
                    scanResult.setClsName(file.getName());
                    scanResult.setVersion(serviceInfo.getVersion());
                    scanResult.setBeanId(serviceInfo.getBeanId());
                    res.add(scanResult);
                }
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private ScanResult parseLine(String code, String beanId) {
        ScanResult scanResult = new ScanResult();
        int bpos = code.indexOf(beanId);
        if (bpos == -1)
            return scanResult;
        int epos = code.indexOf("(", bpos);
        if (epos == -1)
            return scanResult;
        scanResult.setMothedName(code.substring(bpos + beanId.length() + 1, epos));
        scanResult.setLineStr(code);
        return scanResult;
    }

    private static void parseModule(String moduleName) {
        try {
            String filePath = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\venus-" + moduleName + "\\trunk\\src\\main\\resources\\spring\\venus-" + moduleName + "-dubbo-consumer.xml";
            InputStream inputStream = new FileInputStream(filePath);
            AnalysisXMLForDubbo analysisXML = new AnalysisXMLForDubbo();
            List<ServiceInfo> datas = analysisXML.parserXml(inputStream);
            log.info("datas:{}", datas);
            for (ServiceInfo data : datas) {
//                log.info("data:{}", data);
                System.out.println(data.getBeanId()+"|"+data.getInfName()+"|"+data.getVersion());
            }

            System.out.println("-----------------------------------------------------");
            String rootPath = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\venus-" + moduleName + "\\trunk\\src\\main\\java";
            ScanFile scan = new ScanFile();
            List<ScanResult> srs = scan.scanParseFile(new File(rootPath), datas);
//            log.info("srs:{}", srs);
/*            Map<String, String> result = new HashMap<>();
            for (ScanResult sr : srs) {
//                log.info("sr:{}", sr);
//                System.out.println(sr);
                String res = result.put(sr.getBeanId() + "_" + sr.getMothedName() + "_" +sr.getVersion(), sr.getLineStr());
            }

            for (Map.Entry<String, String> entry : result.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }*/

            Collections.sort(srs);
            for (ScanResult sr : srs) {
                System.out.println(sr.getBeanId() + "|" + sr.getVersion() + "|" + sr.getMothedName() + "|--|" + sr.getClsName() + "|" + sr.getLineNum());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        parseModule("trade");

    }
}

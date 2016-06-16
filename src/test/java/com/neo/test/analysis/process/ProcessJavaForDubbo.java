package com.neo.test.analysis.process;

import com.neo.test.analysis.ScanResult;
import com.neo.test.analysis.ServiceInfo;
import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.result.BaseResult;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neowyp on 2016/6/13.
 * Author   : wangyunpeng
 * Date     : 2016/6/13
 * Time     : 12:10
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class ProcessJavaForDubbo implements IProcessScan {
    @Override
    public List<BaseResult> process(File file, BaseCondition condition) {
//        log.info("file:{}, condition:{}", file.getAbsoluteFile(), condition);
        List<BaseResult> rts = new ArrayList<BaseResult>();
        try {
            if (null == file)
                return rts;
            if (!file.isFile())
                return rts;
            String matchesStr = (String) condition.getParam("FILENAME_REGEX");
            if (!file.getName().matches(matchesStr)) { // ".*\\.(?i)java"
                return rts;
            }
//            log.info("file:{}, condition:{}", file.getAbsoluteFile(), condition);
            FileReader reader = new FileReader(file);
//            BufferedReader br = new BufferedReader(reader);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String str = null;
            int line = 0;
            ServiceInfo serviceInfo = (ServiceInfo) condition.getParam("SERVICEINFO");
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
                    rts.add(scanResult);
                }
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            log.error("file:{}", file, e);
        }
        return rts;
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
}

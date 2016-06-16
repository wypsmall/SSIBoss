package com.neo.test.analysis.scan;

import com.neo.test.analysis.ScanResult;
import com.neo.test.analysis.ServiceInfo;
import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.process.IProcessScan;
import com.neo.test.analysis.process.ProcessDubboConsumer;
import com.neo.test.analysis.process.ProcessJavaForDubbo;
import com.neo.test.analysis.result.BaseResult;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neowyp on 2016/6/13.
 * Author   : wangyunpeng
 * Date     : 2016/6/13
 * Time     : 14:11
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class ScanJavaFile extends AbstractScanFile {
    public final static String DUBBO_CONSUMER = "dubbo:reference";

    public ScanJavaFile(IProcessScan processScan) {
        super(processScan);
    }

    public static void main(String[] args) {
        try {
            String filePath = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\venus-trade\\trunk\\src\\main\\resources\\spring\\";
            ProcessDubboConsumer conProcess = new ProcessDubboConsumer();
            ScanXmlFile scan = new ScanXmlFile(conProcess);
            BaseCondition condition = new BaseCondition();
            condition.setParam("dubbo", DUBBO_CONSUMER);
            condition.setParam("FILENAME_REGEX", ".*\\.(?i)xml");
            List<BaseResult> rts =  scan.scanByCdt(new File(filePath), condition);
            List<BaseCondition> bcs = new ArrayList<>();
            log.info("--------------------");
            int count = 0;
            for (BaseResult rt : rts) {
                ServiceInfo serviceInfo = (ServiceInfo) rt;
                BaseCondition baseCondition = new BaseCondition();
                baseCondition.setParam("SERVICEINFO", rt);
                baseCondition.setParam("FILENAME_REGEX", ".*\\.(?i)java");
                bcs.add(baseCondition);

                log.info("{}|{}|{}|{}", count, serviceInfo.getBeanId(), serviceInfo.getInfName(), serviceInfo.getVersion());
                count++;
            }
            log.info("--------------------");
            count = 0;
            String moduleName = "trade";
            String rootPath = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\venus-" + moduleName + "\\trunk\\src\\main\\java";
            ProcessJavaForDubbo dubboProcess = new ProcessJavaForDubbo();
            ScanJavaFile scanJava = new ScanJavaFile(dubboProcess);

            rts = scanJava.scanRootByCdts(new File(rootPath), bcs);
            for (BaseResult rt : rts) {
                ScanResult sr = (ScanResult) rt;

                log.info("{}|{}|{}|{}|{}|{}", count, sr.getBeanId(), sr.getVersion(), sr.getMothedName(), sr.getClsName(), sr.getLineNum());
                count++;
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
}

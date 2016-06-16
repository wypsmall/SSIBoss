package com.neo.test.analysis.scan;

import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.process.IProcessScan;
import com.neo.test.analysis.process.ProcessPomDepend;
import com.neo.test.analysis.result.BaseResult;
import com.neo.test.analysis.result.ProDependency;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * Created by neowyp on 2016/6/13.
 * Author   : wangyunpeng
 * Date     : 2016/6/13
 * Time     : 12:03
 * Version  : V1.0
 * Desc     : 解析xml中的分析dubbo服务提供者和消费者的版本号
 */
@Slf4j
public class ScanPomFile extends AbstractScanFile {

    public final static String DUBBO_PROVIDER = "dubbo:service";
    public final static String DUBBO_CONSUMER = "dubbo:reference";

    public ScanPomFile(IProcessScan processScan) {
        super(processScan);
    }

    public static void main(String[] args) {
        try {
            String filePath = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\";
            ProcessPomDepend pomProcess = new ProcessPomDepend();
            ScanPomFile scan = new ScanPomFile(pomProcess);

            scan.tagPoint("begin");
            BaseCondition condition = new BaseCondition();
            condition.setParam("FILENAME_REGEX", "pom.xml");
            List<BaseResult> rts = scan.scanByCdt(new File(filePath), condition);
            scan.tagPoint("scan");
            int count = 0;
            for (BaseResult rt : rts) {
                log.info("--------------------");
                ProDependency dependency = (ProDependency) rt;
                log.info("{}|{}-{}.{}", count, dependency.getArtifactId(), dependency.getVersion(), dependency.getPackaging());
                if (dependency.getChildren() != null) {
                    int chcount = 0;
                    for (ProDependency child : dependency.getChildren()) {
                        log.info("   {}|{}-{}.{}", chcount, child.getArtifactId(), child.getVersion(), child.getPackaging());
                        chcount++;
                    }
                }
                count++;
            }
            scan.tagPoint("print");
            scan.printCost();
        } catch (Exception e) {
            log.error("", e);
        }
    }
}

package com.neo.test.analysis.scan;

import com.neo.test.analysis.ServiceInfo;
import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.process.IProcessScan;
import com.neo.test.analysis.process.ProcessDubboConsumer;
import com.neo.test.analysis.process.ProcessDubboProvider;
import com.neo.test.analysis.result.BaseResult;
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
public class ScanXmlFile extends AbstractScanFile {

    public final static String DUBBO_PROVIDER = "dubbo:service";
    public final static String DUBBO_CONSUMER = "dubbo:reference";
    public ScanXmlFile(IProcessScan processScan) {
        super(processScan);
    }

    public static void main(String[] args) {
        try {
            String filePath = "D:\\Code\\Code_IntellijIdea\\ReviewCode\\gomeo2o_pro\\venus-trade\\trunk\\src\\main\\resources\\spring\\";
            ProcessDubboConsumer conProcess = new ProcessDubboConsumer();
            ScanXmlFile scan = new ScanXmlFile(conProcess);
            BaseCondition condition = new BaseCondition();
            condition.setParam("dubbo", DUBBO_CONSUMER);
            List<BaseResult> rts =  scan.scanByCdt(new File(filePath), condition);
            log.info("--------------------");
            int count = 0;
            for (BaseResult rt : rts) {
                ServiceInfo serviceInfo = (ServiceInfo) rt;
                log.info("{}|{}|{}|{}", count, serviceInfo.getBeanId(), serviceInfo.getInfName(), serviceInfo.getVersion());
                count++;
            }
            log.info("--------------------");
            ProcessDubboProvider proProcess = new ProcessDubboProvider();
            scan = new ScanXmlFile(proProcess);
            condition.setParam("dubbo", DUBBO_PROVIDER);
            rts =  scan.scanByCdt(new File(filePath), condition);
            count = 0;
            for (BaseResult rt : rts) {
                ServiceInfo serviceInfo = (ServiceInfo) rt;
                log.info("{}|{}|{}|{}", count, serviceInfo.getBeanId(), serviceInfo.getInfName(), serviceInfo.getVersion());
                count++;
            }

        } catch (Exception e) {
            log.error("",e);
        }
    }
}

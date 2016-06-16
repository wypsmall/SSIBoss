package com.neo.test.analysis.scan;

import com.neo.test.analysis.process.IProcessScan;
import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.result.BaseResult;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;

/**
 * Created by neowyp on 2016/6/13.
 * Author   : wangyunpeng
 * Date     : 2016/6/13
 * Time     : 11:19
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public abstract class AbstractScanFile {
    protected IProcessScan processScan;

    private LinkedHashMap<String, Long> cost = new LinkedHashMap<>();

    public void tagPoint(String tag) {
        cost.put(tag, System.currentTimeMillis());
    }
    public void printCost() {
        StringBuffer buf = new StringBuffer();
        Long last = 0L;
        Long cur = 0L;
        for (Map.Entry<String, Long> entry : cost.entrySet()) {
            cur = entry.getValue();
            buf.append(entry.getKey());
            buf.append(":");
            buf.append(cur-last);
            buf.append("ms | ");
            last = entry.getValue();

        }
        log.info("cost:[{}]", buf.toString());
    }
    public AbstractScanFile() {

    }

    public AbstractScanFile(IProcessScan processScan) {
        this.processScan = processScan;
    }

    public List<BaseResult> scanByCdt(File path, BaseCondition condition) {
        List<BaseResult> results = new ArrayList<BaseResult>();
        File[] fs = path.listFiles();
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isFile()) {
                List<BaseResult> fileScanRes = processScan.process(fs[i], condition);
                if (null != fileScanRes)
                    results.addAll(fileScanRes);
            }

            if (fs[i].isDirectory()) {
                try {
                    List<BaseResult> dirScanRes = scanByCdt(fs[i], condition);
                    if (null != dirScanRes)
                        results.addAll(dirScanRes);
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
        return results;

    }

    public List<BaseResult> scanRootByCdts(File path, List<BaseCondition> conditions) {
        List<BaseResult> baseResults = new ArrayList<BaseResult>();
        for (BaseCondition condition : conditions) {
            List<BaseResult> childResults = scanByCdt(path, condition);
            baseResults.addAll(childResults);
        }
        return baseResults;
    }

}

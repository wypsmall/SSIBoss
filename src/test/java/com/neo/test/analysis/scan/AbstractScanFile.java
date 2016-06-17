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
 * Desc     : 抽象类，定义如何扫描文件，例如：解析xml、逐行分析源码
 */
@Slf4j
public abstract class AbstractScanFile {
    //扫描处理接口类
    protected IProcessScan processScan;
    //用于记录耗时
    private LinkedHashMap<String, Long> cost = new LinkedHashMap<>();

    /**
     * 向cost追加记录点名称和当前时间
     * @param tag 记录点名称
     */
    public void tagPoint(String tag) {
        cost.put(tag, System.currentTimeMillis());
    }

    /**
     * 打印操作耗时，单位ms
     */
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

    /**
     * 根据条件condition，扫描或解析file内容，返回结果数据
     * @param file 待处理的文件/文件夹对象
     * @param condition 扫描条件，或约束条件
     * @return 结果集合
     */
    public List<BaseResult> scanByCdt(File file, BaseCondition condition) {
        List<BaseResult> results = new ArrayList<BaseResult>();
        File[] fs = file.listFiles();
        //递归处理到没有子文件夹为止
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isFile()) {
                //文件处理交由processScan对象，并返回结果集
                List<BaseResult> fileScanRes = processScan.process(fs[i], condition);
                if (null != fileScanRes)
                    results.addAll(fileScanRes);
            }
            //如果是文件夹则采用递归方式处理
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

    /**
     * 多条件扫描
     * @param file 待处理的文件/文件夹对象
     * @param conditions 约束条件集合
     * @return 结果集合
     */
    public List<BaseResult> scanRootByCdts(File file, List<BaseCondition> conditions) {
        List<BaseResult> baseResults = new ArrayList<BaseResult>();
        //循环所有约束条件一次查询file
        for (BaseCondition condition : conditions) {
            List<BaseResult> childResults = scanByCdt(file, condition);
            baseResults.addAll(childResults);
        }
        return baseResults;
    }

}

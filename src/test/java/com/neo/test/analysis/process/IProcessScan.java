package com.neo.test.analysis.process;

import com.neo.test.analysis.condition.BaseCondition;
import com.neo.test.analysis.result.BaseResult;

import java.io.File;
import java.util.List;

/**
 * Created by neowyp on 2016/6/13.
 * Author   : wangyunpeng
 * Date     : 2016/6/13
 * Time     : 11:43
 * Version  : V1.0
 * Desc     : 扫描处理接口类
 */
public interface IProcessScan {
    /**
     * 根据condition处理file，返回结果集
     * @param file  待处理文件对象
     * @param condition 约束条件
     * @return 返回List<BaseResult>数据集合
     */
    public List<BaseResult> process(File file, BaseCondition condition);
}

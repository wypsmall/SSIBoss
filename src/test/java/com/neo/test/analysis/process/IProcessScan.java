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
 * Desc     :
 */
public interface IProcessScan {
    public List<BaseResult> process(File file, BaseCondition condition);
}

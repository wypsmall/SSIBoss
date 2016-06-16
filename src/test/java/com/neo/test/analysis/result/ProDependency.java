package com.neo.test.analysis.result;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neowyp on 2016/6/13.
 * Author   : wangyunpeng
 * Date     : 2016/6/13
 * Time     : 16:34
 * Version  : V1.0
 * Desc     :
 */
@Data
@ToString
public class ProDependency extends BaseResult {

    private static final long serialVersionUID = -1413867911164234738L;
    private String fileName;
    private String groupId;
    private String artifactId;
    private String version;
    private String packaging = "jar";

    private List<ProDependency> children = new ArrayList<ProDependency>();

    public boolean addChild(ProDependency dependency) {
        if(null != children) {
            children.add(dependency);
            return true;
        }
        return false;
    }
}

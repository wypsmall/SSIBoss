package com.neo.test.analysis;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by neowyp on 2016/6/12.
 * Author   : wangyunpeng
 * Date     : 2016/6/12
 * Time     : 13:49
 * Version  : V1.0
 * Desc     :
 */
@Data
@ToString
public class ScanResult implements Serializable , Comparable<ScanResult> {

    private static final long serialVersionUID = -7803671836208618086L;

    private String beanId;  //bean id
    private String version; //dubbo service version
    private String infName; //interface class name

    private String clsName; //appear class name
    private String mothedName;  //invoke function method
    private Integer lineNum;    //line number
    private String lineStr;     //appear code line

    @Override
    public int compareTo(ScanResult o) {
        return (beanId+version+mothedName).compareTo(o.getBeanId()+o.getVersion()+o.getMothedName());
    }
}

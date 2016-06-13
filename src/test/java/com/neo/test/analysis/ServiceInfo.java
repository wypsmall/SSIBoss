package com.neo.test.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by neowyp on 2016/6/12.
 * Author   : wangyunpeng
 * Date     : 2016/6/12
 * Time     : 12:39
 * Version  : V1.0
 * Desc     :
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInfo implements Serializable {
    private static final long serialVersionUID = -7445737705476368638L;

    private String beanId;  //bean id
    private String beanName;    //bean name
    private String infName; //interface class name
    private String clsName; //implment class name
    private String version; //dubbo service version


}

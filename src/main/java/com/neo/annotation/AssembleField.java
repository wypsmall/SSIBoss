package com.neo.annotation;

import java.lang.annotation.*;

/**
 * Created by neowyp on 2016/3/22.
 */
@Documented
@Target(ElementType.FIELD)  //作用域是属性字段
@Inherited  //可以集成
@Retention(RetentionPolicy.RUNTIME)
public @interface AssembleField { //注解需要通过@符号定义接口
    String relateId() default ""; //关联id
    String resField() default "resultData"; //返回数据集的key
    String opBean() default ""; //级联查询所涉及的bean name
}

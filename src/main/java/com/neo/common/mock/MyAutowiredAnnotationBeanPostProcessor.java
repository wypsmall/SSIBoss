package com.neo.common.mock;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

import java.beans.PropertyDescriptor;

/**
 * Created by neowyp on 2016/3/10.
 */
public class MyAutowiredAnnotationBeanPostProcessor extends AutowiredAnnotationBeanPostProcessor {
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        try {
            System.out.println("=========================================================");
            return super.postProcessPropertyValues(pvs, pds, bean, beanName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

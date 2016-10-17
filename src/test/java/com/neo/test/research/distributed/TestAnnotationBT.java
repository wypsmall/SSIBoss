package com.neo.test.research.distributed;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by neowyp on 2016/10/14.
 * Author   : wangyunpeng
 * Date     : 2016/10/14
 * Time     : 16:34
 * Version  : V1.0
 * Desc     : 单元测试，aop拦截配置见spring-aop.xml
 */
public class TestAnnotationBT {

    public static void main(String[] args) throws Exception{
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-aop.xml");
        OrderService orderService = (OrderService)ctx.getBean("orderService");
        orderService.createOrder();
        ItemService itemService = (ItemService)ctx.getBean("itemService");
        itemService.subStock();
        PromotionService promotionService = (PromotionService)ctx.getBean("promotionService");
        promotionService.subCoupon();

    }
}


package com.neo.test.research.distributed;

import java.lang.reflect.Method;

/**
 * Created by neowyp on 2016/10/14.
 * Author   : wangyunpeng
 * Date     : 2016/10/14
 * Time     : 16:36
 * Version  : V1.0
 * Desc     :  作废
 */
@Deprecated
public class OrderBusi {
    private OrderService orderService;
    private ItemService itemService;
    private PromotionService promotionService;

    public void initBean() {
        orderService = new OrderService();
        itemService = new ItemService();
        promotionService = new PromotionService();
    }

    public void createOrder() throws NoSuchMethodException {
        itemService.subStock(); //扣库存
        promotionService.subCoupon(); //冻结优惠券
        orderService.createOrder(); //生成订单

        Method method = itemService.getClass().getMethod("subStock");
        if(method.isAnnotationPresent(DistributedTransaction.class)) {
            DistributedTransaction dt = method.getAnnotation(DistributedTransaction.class);

        }
    }

    public static void main(String[] args) throws Exception {
        OrderBusi orderBusi = new OrderBusi();
        orderBusi.createOrder();
    }


}

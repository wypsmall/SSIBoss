package com.neo.order.entity;

import com.neo.common.entity.BaseEntity;
import com.neo.user.service.impl.OrderServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by neowyp on 2016/3/11.
 */
@Setter
@Getter
@ToString
public class OrderEntity extends BaseEntity {

    private static final long serialVersionUID = -4305251967400418556L;

    private String orderSN;
    private BigDecimal amount;
    private String extend;

    public static void main(String[] args) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSN("fads");
        BigDecimal amount = new BigDecimal(100.20).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(amount);
        orderEntity.setAmount(amount);
        System.out.println(orderEntity.toString());
    }
}

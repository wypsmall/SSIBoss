package com.neo.order.service.impl;

import com.neo.order.dao.IOrderOpDao;
import com.neo.order.entity.ItemEntity;
import com.neo.order.entity.OrderEntity;
import com.neo.order.service.IItemOpService;
import com.neo.order.service.IOrderOpService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by neowyp on 2016/3/11.
 */
@Service("orderOpService")
public class OrderOpServiceImpl implements IOrderOpService {
    @Autowired
    private IOrderOpDao orderOpDao;

    @Autowired
    private IItemOpService itemOpService;

//    @Transactional
    public OrderEntity createOrder(Integer id) {
        ItemEntity itemEntity = itemOpService.getItemById(id);
        System.out.println("=====> itemEntity : " + itemEntity);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSN("D-" + System.currentTimeMillis());
        orderEntity.setAmount(itemEntity.getPrice());
        orderEntity.setExtend(itemEntity.toString());

        Long ID = orderOpDao.insert(orderEntity);
        orderEntity.setId(ID);

        orderEntity.setOrderSN(orderEntity.getExtend());
        ID = orderOpDao.insert(orderEntity);
        orderEntity.setId(ID);

        return orderEntity;
    }
}

package com.neo.order.dao;

import com.neo.common.dao.BaseDaoImpl;
import com.neo.order.entity.OrderEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by neowyp on 2016/3/11.
 */
@Repository("orderOpDao")
public class OrderOpDaoImpl extends BaseDaoImpl<OrderEntity> implements IOrderOpDao {

}

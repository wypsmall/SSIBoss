package com.neo.test.order;

import com.neo.common.mock.MockitoDependencyInjectionTestExecutionListener;
import com.neo.order.dao.IOrderOpDao;
import com.neo.order.entity.ItemEntity;
import com.neo.order.entity.OrderEntity;
import com.neo.order.service.IItemOpService;
import com.neo.order.service.IOrderOpService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

/**
 * Created by neowyp on 2016/3/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/test-applicationContext-mybatis.xml",
        "classpath*:spring/test-orderAppCtx.xml"})
public class TestOrderService {

    @Autowired
    ApplicationContext ctx;

    @Autowired
    IOrderOpService orderOpService;

    @Autowired
    IOrderOpDao orderOpDao;

//    @Mock
//    IItemOpService itemOpService;

    @Before
    public void initOperate() {
//        MockitoAnnotations.initMocks(this);
        IItemOpService itemOpService = (IItemOpService) ctx.getBean("itemOpService");
        when(itemOpService.getItemById(20)).thenReturn(getItemEntity());
    }

    @Test
    public void testCreateOrder() {
/*        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSN("D-" + System.currentTimeMillis());
        orderEntity.setAmount(new BigDecimal("2.00"));
        orderEntity.setExtend(orderEntity.toString());
        orderOpDao.insert(orderEntity);*/
        orderOpService.createOrder(20);
    }

    private static ItemEntity getItemEntity() {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(123L);
        itemEntity.setPrice(new BigDecimal("5.00"));
        itemEntity.setSkuId(100);
        itemEntity.setSkuName("fdas");
        return itemEntity;

    }
}

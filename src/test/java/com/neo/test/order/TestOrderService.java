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


//    已经通过spring的xml文件创建mock对象，所以这里不需要在手工mock了
//    @Mock
//    IItemOpService itemOpService;

    @Before
    public void initOperate() {
//        不需要在初始化mock注解，如果有才初始化，目前当前类是没有注解mock所以可以注释
//        MockitoAnnotations.initMocks(this);

//        如果像mock方法，那么就需要先从spring容器取出，在定义mock方法的返回值
        IItemOpService itemOpService = (IItemOpService) ctx.getBean("itemOpService");

//        在这里mock itemOpService的getItemById方法调用的返回值
        when(itemOpService.getItemById(20)).thenReturn(getItemEntity());
    }

    @Test
    public void testCreateOrder() {
//        事务配置是可以生效的，因为orderOpService是spring容器创建，所以通过声明式事务是生效了
//        如果createOrder方法的入参不是20，那么when(itemOpService.getItemById(20)).thenReturn(getItemEntity());就不生效
        orderOpService.createOrder(20);
    }

    /**
     * @return mock item entity
     */
    private static ItemEntity getItemEntity() {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(123L);
        itemEntity.setPrice(new BigDecimal("5.00"));
        itemEntity.setSkuId(100);
        itemEntity.setSkuName("fdas");
        return itemEntity;

    }
}

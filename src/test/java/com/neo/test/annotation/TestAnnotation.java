package com.neo.test.annotation;

import com.neo.annotation.AssembleField;
import com.neo.annotation.ShopDO;
import com.neo.annotation.VShopDao;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neowyp on 2016/3/22.
 */
@Slf4j
public class TestAnnotation {

    public static Map<String, Object> beans = new HashMap<String, Object>();
    //模拟初始bean
    static {
        beans.put("vshopDao", new VShopDao());
    }

    public static void main(String[] args) {
        try {
            ShopDO shopDO = new ShopDO(); //new shops实体
            shopDO.setId(203L); //设置shopid
            Class<ShopDO> shopDOClass = ShopDO.class;
            Field[] fields = shopDOClass.getDeclaredFields(); //获取所有声明的字段
            for (Field field : fields) {
                if (field.isAnnotationPresent(AssembleField.class)) { //判断是否有注释，匹配AssembleField类型的注释
                    log.info("{} has Annotation {}", field, AssembleField.class);
                    AssembleField assembleField = (AssembleField) field.getAnnotation(AssembleField.class);
                    if (null != assembleField) {
                        log.info("{} annotation value is {}", field, assembleField);
                    }
                    //装配的店铺信息之前
                    log.info("assemble before {}", shopDO);
                    //装配数据vshop自动会回写到shopDO
                    assembleInfo(shopDO, field, assembleField);
                    //打印输出结果
                    log.info("assemble after  {}", shopDO);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 根据注释装配数据
     * @param shopDO 店铺DO
     * @param field 需要装配的field
     * @param assembleField 注释的field
     * @throws Exception
     */
    private static void assembleInfo(ShopDO shopDO, Field field, AssembleField assembleField) throws Exception {
        Object bean = beans.get(assembleField.opBean());
        if (null != bean) {
            Class clz = bean.getClass();
            Field primaryField = shopDO.getClass().getDeclaredField("id");
            Method method = clz.getMethod("getByShopId", new Class[]{Long.class});

            primaryField.setAccessible(true);
            Object res = method.invoke(bean, new Object[]{primaryField.get(shopDO)});

            field.setAccessible(true);
            field.set(shopDO, res);
        }
    }
}

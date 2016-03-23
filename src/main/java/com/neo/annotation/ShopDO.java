package com.neo.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Field;

/**
 * Created by neowyp on 2016/3/22.
 */
@Data   //lombok set get
@ToString   //lombok
@NoArgsConstructor  //lombok 无参构造函数
@AllArgsConstructor //lombok 全参构造函数
public class ShopDO {
    private Long id;

    @AssembleField(relateId = "shopId", opBean="vshopDao")
    private VShopDO vShopDO;

    public static void main(String[] args) {
        try {
            ShopDO shopDO = new ShopDO();
            Field[] fields = shopDO.getClass().getDeclaredFields();
            for (Field field : fields) {
                System.out.println(field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

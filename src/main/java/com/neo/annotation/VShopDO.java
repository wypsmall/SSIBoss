package com.neo.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by neowyp on 2016/3/22.
 * 参见ShopDO
 */@Data
   @ToString
   @NoArgsConstructor
   @AllArgsConstructor
public class VShopDO {
    private Long id;
    private Long shopId;
    private String vshopName;
}

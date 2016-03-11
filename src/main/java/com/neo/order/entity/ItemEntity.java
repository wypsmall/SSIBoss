package com.neo.order.entity;

import com.neo.common.entity.BaseEntity;
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
public class ItemEntity extends BaseEntity {
    private static final long serialVersionUID = -4950354867152469044L;

    private Integer skuId;
    private String  skuName;
    private BigDecimal price;
}

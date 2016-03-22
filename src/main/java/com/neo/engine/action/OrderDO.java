package com.neo.engine.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by neowyp on 2016/3/22.
 */
@Data
@ToString
@AllArgsConstructor
public class OrderDO implements Serializable {
    private static final long serialVersionUID = -4076042935267362068L;

    private Long id;
    private Long shopid;
    private Long amount;
    private Date createDt = new Date();
    private Date updateDt = new Date();
    private String status;
}

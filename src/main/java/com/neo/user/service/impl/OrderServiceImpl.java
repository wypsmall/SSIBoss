package com.neo.user.service.impl;

import com.neo.user.service.IEmployeeService;
import com.neo.user.service.IOrderService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by neowyp on 2016/3/9.
 */
@Service("orderService")

public class OrderServiceImpl implements IOrderService {

//    @Autowired(required = false)
    @Autowired
    @Setter
    private IEmployeeService employeeService;


    @Override
    public String getOrderEmployeeInfo() {
        return employeeService.getEmployeeId();
    }
}

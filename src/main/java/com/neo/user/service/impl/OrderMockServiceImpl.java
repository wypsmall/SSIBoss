package com.neo.user.service.impl;

import com.neo.user.dao.IUserDao;
import com.neo.user.service.IEmployeeService;
import com.neo.user.service.IOrderMockService;
import com.neo.user.service.IOrderService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by neowyp on 2016/3/9.
 */
@Service("orderMockService")
public class OrderMockServiceImpl implements IOrderMockService {

//    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IUserDao userDao;

    @Override
    public String getOrderEmployeeInfo() {
        return employeeService.getEmployeeId();
    }
}

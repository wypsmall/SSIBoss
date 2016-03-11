package com.neo.test.service;

import com.neo.user.service.IEmployeeService;
import org.springframework.stereotype.Component;

/**
 * Created by neowyp on 2016/3/10.
 */
//@Component("employeeService")
public class MockEmployeeService implements IEmployeeService {
    @Override
    public String getEmployeeId() {
        return null;
    }
}

package com.neo.user.entity;

import com.neo.common.entity.BaseEntity;

import java.io.Serializable;

/**
 * Created by neowyp on 2016/2/23.
 */
public class User extends BaseEntity {

    private static final long serialVersionUID = 4828672110674725231L;
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

package com.neo.drools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by neowyp on 2016/3/25.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {

    private static final long serialVersionUID = 3271899877313373665L;

    private Long id;
    private String name;
}

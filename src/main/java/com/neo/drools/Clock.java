package com.neo.drools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by neowyp on 2016/3/24.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Clock {
    private int hour;
    private int minute;
    private int second;
}

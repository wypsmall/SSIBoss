package com.neo.engine.action;

import lombok.ToString;

/**
 * Created by neowyp on 2016/3/21.
 */
@ToString
public enum BusiStatus {

    BS_START("BS_ST", "起始状态"),
    BS_PREPROCESS("BS_PE","预处理状态"),
    BS_PERSISTENT("BS_DB","预处理状态"),
    BS_POSTPROCESS("BS_PT","后处理状态"),
    BS_END("BS_ED", "终结状态");


    public final String value;

    public final String display;

    BusiStatus(String value, String display) {
        this.value = value;
        this.display = display;
    }

    public String value() {
        return value;
    }

    public static BusiStatus from(String value) {
        for (BusiStatus busiStatus : BusiStatus.values()) {
            if (busiStatus.value.equals(value))
                return busiStatus;
        }
        return null;
    }

    public static void main(String[] args) {
        String status = BusiStatus.BS_START.value();
        System.out.println(status);
        System.out.println(BusiStatus.BS_END);
    }
}

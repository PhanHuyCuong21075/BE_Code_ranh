package com.cuongph.be_code.common.enums;

public enum ELimitRunTime {

    UNLIMITED(-1),
    SHORT(3000),
    MEDIUM(5000),
    LONG(15000);

    public enum LimitHandle{
        CANCEL,
        LOGGING
    }
    private final int value;
    ELimitRunTime(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
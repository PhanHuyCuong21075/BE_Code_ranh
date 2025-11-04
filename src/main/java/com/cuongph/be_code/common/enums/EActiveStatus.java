package com.cuongph.be_code.common.enums;

public enum EActiveStatus {
    ACTIVE(1L),
    IN_ACTIVE(0L);

    public final Long value;

    EActiveStatus(Long value) {
        this.value = value;
    }
}

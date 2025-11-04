package com.cuongph.be_code.common.antations;

import com.cuongph.be_code.common.enums.ELimitRunTime;
import com.cuongph.be_code.exception.BusinessException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseSafeRunning {
    boolean isThrowException() default false;

    Class<? extends Throwable> alwaysThrow() default BusinessException.class;

    ELimitRunTime limitRunTime() default ELimitRunTime.UNLIMITED;

    ELimitRunTime.LimitHandle limitHandle() default ELimitRunTime.LimitHandle.LOGGING;
}

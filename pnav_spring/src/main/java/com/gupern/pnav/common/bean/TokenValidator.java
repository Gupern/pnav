package com.gupern.pnav.common.bean;

import java.lang.annotation.*;

/**
 * @description 校验session的注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenValidator {
}

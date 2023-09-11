package com.fgj.config;

import java.lang.annotation.*;

/**
 * @author fu
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {
    
}

package com.dou.adm.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

import static com.dou.adm.shared.TargetDSName.DS_DEFAULT;

/**
 * Created by Tu.Tran on 9/28/2018.
 */

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String value() default DS_DEFAULT;
}

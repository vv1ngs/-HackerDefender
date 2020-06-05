package org.hackDefender.interceptor;

import java.lang.annotation.*;

/**
 * @author vvings
 * @version 2020/6/5 10:13
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestLogin {
    String desc() default Permission.REQUEST_LOGIN;
}

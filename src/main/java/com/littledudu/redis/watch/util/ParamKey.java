/**
 * 
 */
package com.littledudu.redis.watch.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @date 2015-9-25
 * @author hujinjun
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamKey {
    String value() default "";
}

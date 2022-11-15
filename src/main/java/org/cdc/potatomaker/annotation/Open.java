package org.cdc.potatomaker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * e-mail: 3154934427@qq.com
 * 开放注解,只有被开发的类才可以访问
 *
 * @author cdc123
 * @classname Open
 * @date 2022/11/15 13:48
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Open {
}

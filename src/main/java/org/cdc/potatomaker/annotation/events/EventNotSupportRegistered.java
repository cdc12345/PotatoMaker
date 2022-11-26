package org.cdc.potatomaker.annotation.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * e-mail: 3154934427@qq.com
 * 无法用Events注册的事件用此注解
 *
 * @author cdc123
 * @classname EventNotSupportRegistered
 * @date 2022/11/19 21:59
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventNotSupportRegistered {
}

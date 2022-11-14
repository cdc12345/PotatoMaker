package org.cdc.potatomaker.events;

import lombok.Data;

/**
 * e-mail: 3154934427@qq.com
 * 事件超类
 *
 * @author cdc123
 * @classname Event
 * @date 2022/11/13 22:36
 */
@Data
public abstract class Event {
    private Object source;
}

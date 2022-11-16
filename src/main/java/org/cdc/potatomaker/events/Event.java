package org.cdc.potatomaker.events;

import lombok.Data;
import lombok.Getter;

/**
 * e-mail: 3154934427@qq.com
 * 事件超类
 *
 * @author cdc123
 * @classname Event
 * @date 2022/11/13 22:36
 */
@Data
@Getter
public abstract class Event {
    protected Object source;

    public Event(Object source){
        this.source = source;
    }
}

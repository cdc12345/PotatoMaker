package org.cdc.potatomaker.events;

import org.cdc.potatomaker.annotation.Open;

/**
 * e-mail: 3154934427@qq.com
 * 程序退出事件
 *
 * @author cdc123
 * @classname ApplicationExitEvent
 * @date 2022/11/16 7:45
 */
@Open
public class ApplicationExitEvent extends Event{
    public ApplicationExitEvent(Object source) {
        super(source);
    }
}

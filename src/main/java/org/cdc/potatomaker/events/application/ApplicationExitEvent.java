package org.cdc.potatomaker.events.application;


import org.cdc.potatomaker.events.Event;

/**
 * e-mail: 3154934427@qq.com
 * 程序退出事件
 *
 * @author cdc123
 * @classname ApplicationExitEvent
 * @date 2022/11/16 7:45
 */
public class ApplicationExitEvent extends Event {
    public ApplicationExitEvent(Object source) {
        super(source);
    }
}

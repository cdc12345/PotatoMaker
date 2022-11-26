package org.cdc.potatomaker.events.application;

import lombok.Getter;
import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.events.Event;

import java.util.List;

/**
 * e-mail: 3154934427@qq.com
 * 程序启动伴随事件
 *
 * @author cdc123
 * @classname ApplicationInitEvent
 * @date 2022/11/16 7:36
 */
@Getter
public class ApplicationInitEvent extends Event {
    final List<String> args;
    public ApplicationInitEvent(Object source, List<String> args) {
        super(source);
        this.args = args;
    }
}

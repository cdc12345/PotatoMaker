package org.cdc.potatomaker.events;

import lombok.Getter;
import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.ui.SplashScreen;

import java.util.List;

/**
 * e-mail: 3154934427@qq.com
 * 如果启动有参数,则调用此事件
 *
 * @author cdc123
 * @classname WithArgsStartingEvent
 * @date 2022/11/15 14:38
 */
@Getter
@Open
public class WithArgsStartingEvent extends Event {
    private final List<String> arguments;
    private final SplashScreen splashScreen;

    public WithArgsStartingEvent(Object source, List<String> arguments, SplashScreen splashScreen) {
        super(source);
        this.arguments = arguments;
        this.splashScreen = splashScreen;
    }
}

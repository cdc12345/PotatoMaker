package org.cdc.potatomaker.events;

import lombok.Getter;
import org.cdc.potatomaker.ui.SplashScreen;

/**
 * e-mail: 3154934427@qq.com
 * 当程序启动
 *
 * @author cdc123
 * @classname ApplicationStartingEvent
 * @date 2022/11/15 14:44
 */
@Getter
public class WithoutArgsStartingEvent extends Event{
    private SplashScreen splashScreen;
    public WithoutArgsStartingEvent(Object source,SplashScreen splashScreen) {
        super(source);
        this.splashScreen = splashScreen;
    }
}

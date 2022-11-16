package org.cdc.potatomaker.events;

import lombok.Getter;
import org.cdc.potatomaker.util.resource.UIRE;

/**
 * e-mail: 3154934427@qq.com
 * 主题初始化事件
 *
 * @author cdc123
 * @classname ThemeInitEvent
 * @date 2022/11/16 7:26
 */
@Getter
public class ThemeInitEvent extends Event{
    final UIRE UIRE;
    public ThemeInitEvent(Object source,UIRE uire) {
        super(source);
        this.UIRE = uire;
    }
}

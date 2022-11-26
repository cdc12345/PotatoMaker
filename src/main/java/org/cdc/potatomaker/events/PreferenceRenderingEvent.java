package org.cdc.potatomaker.events;

import lombok.Getter;
import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.annotation.events.EventNotSupportRegistered;
import org.cdc.potatomaker.preference.Preferences;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * e-mail: 3154934427@qq.com
 * 设置条目正在渲染事件
 *
 * @author cdc123
 * @classname PreferenceRenderingEvent
 * @date 2022/11/13 22:38
 */
@EventNotSupportRegistered
@Getter
public final class PreferenceRenderingEvent extends Event {
    private final Preferences cachePreferences;

    public PreferenceRenderingEvent(Object source, Preferences cachePreferences) {
        super(source);

        this.cachePreferences = cachePreferences;
    }
}

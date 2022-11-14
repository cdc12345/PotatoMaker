package org.cdc.potatomaker.preference.render;

import org.cdc.potatomaker.events.Event;
import org.cdc.potatomaker.events.PreferenceRenderingEvent;
import org.cdc.potatomaker.preference.Preferences;
import org.cdc.potatomaker.ui.component.ComponentRender;

import java.awt.*;

/**
 * e-mail: 3154934427@qq.com
 * 设置渲染器
 *
 * @author cdc123
 * @classname PreferenceRender
 * @date 2022/11/14 8:30
 */
public interface PreferenceRender extends ComponentRender {
    default Component getComponent(Event event){
        return getComponent(event);
    }

    Component getComponent(PreferenceRenderingEvent event);

    Preferences storePreference();
}

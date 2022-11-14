package org.cdc.potatomaker.preference.render;

import org.cdc.potatomaker.events.Event;
import org.cdc.potatomaker.events.PreferenceRenderingEvent;
import org.cdc.potatomaker.ui.component.ComponentRender;

import java.awt.*;

/**
 * e-mail: 3154934427@qq.com
 * 设置条目渲染器
 * 此为配置系最低一级的渲染器
 *
 * @author cdc123
 * @classname PreferenceCellRender
 * @date 2022/11/13 22:35
 */
public interface PreferenceCellRender extends ComponentRender {
    int minPriority = 0;
    default int getPriority(){
        return 0;
    }

    default Component getComponent(Event event){
        return getComponent(event);
    }

    Component getComponent(PreferenceRenderingEvent event);
}

package org.cdc.potatomaker.preference.render;

import org.cdc.potatomaker.events.Event;
import org.cdc.potatomaker.events.PreferenceRenderingEvent;
import org.cdc.potatomaker.preference.PreferenceCell;
import org.cdc.potatomaker.ui.component.ComponentRender;

import java.awt.*;
import java.util.ArrayList;

/**
 * e-mail: 3154934427@qq.com
 * 设置条目渲染器
 *
 * @author cdc123
 * @classname PreferenceSectionRender
 * @date 2022/11/14 8:36
 */
public interface PreferenceSectionRender extends ComponentRender {
    default Component getComponent(Event event){
        return getComponent(event);
    }

    Component getComponent(PreferenceRenderingEvent event);

    void setCells(ArrayList<PreferenceCell> cells);
}

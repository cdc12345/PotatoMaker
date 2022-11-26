package org.cdc.potatomaker.preference.sections;

import org.cdc.potatomaker.events.PreferenceRenderingEvent;

import java.awt.*;

/**
 * e-mail: 3154934427@qq.com
 * 设置栏目渲染器
 *
 * @author cdc123
 * @classname PreferenceSectionRender
 * @date 2022/11/26 13:52
 */
public interface PreferenceSectionRender {
    Component getSectionComponent(String section);

    Component getCellComponent(PreferenceRenderingEvent event);
}

package org.cdc.potatomaker.ui.component;

import org.cdc.potatomaker.events.Event;

import java.awt.*;

/**
 * e-mail: 3154934427@qq.com
 *
 *
 * @author cdc123
 * @classname CellRender
 * @date 2022/11/14 8:06
 */
public interface ComponentRender {
    Component getComponent(Event event);
}

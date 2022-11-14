package org.cdc.potatomaker.preference;

import org.cdc.potatomaker.events.PreferenceRenderingEvent;
import org.cdc.potatomaker.preference.render.PreferenceSectionRender;
import org.cdc.potatomaker.ui.component.VerticalPanel;

import java.awt.*;
import java.util.ArrayList;

/**
 * e-mail: 3154934427@qq.com
 * 默认条目渲染器
 *
 * @author cdc123
 * @classname DefaultSectionRender
 * @date 2022/11/14 9:29
 */
public class DefaultPreferenceSectionRender implements PreferenceSectionRender {

    ArrayList<PreferenceCell> cells;

    @Override
    public Component getComponent(PreferenceRenderingEvent event) {
        //TODO
        var panel = new VerticalPanel();
        if (cells == null)
            return panel;
        return panel;
    }

    @Override
    public void setCells(ArrayList<PreferenceCell> cells) {
        this.cells = cells;
    }
}

package org.cdc.potatomaker.preference.sections;

import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.events.PreferenceRenderingEvent;
import org.cdc.potatomaker.ui.component.PluginsPanel;
import org.cdc.potatomaker.ui.component.VerticalPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * e-mail: 3154934427@qq.com
 * 默认设置渲染器
 *
 * @author cdc123
 * @classname DefaultPreferenceRender
 * @date 2022/11/26 14:02
 */
@Open
public class DefaultPreferenceSectionRender implements PreferenceSectionRender{
    protected HashMap<String,Component> defaultComponents = new HashMap<>();

    protected DefaultPreferenceSectionModel model;
    public DefaultPreferenceSectionRender(DefaultPreferenceSectionModel model){
        this.model = model;
        defaultComponents.put("plugins",new PluginsPanel());
    }

    @Override
    public Component getSectionComponent(String section) {
        if (defaultComponents.containsKey(section)) return defaultComponents.get(section);
        VerticalPanel result = new VerticalPanel();
        var cells = model.getCells();
        for (String cell:cells){
            result.add(getCellComponent(new PreferenceRenderingEvent(this,model.preferences)));
        }
        return result;
    }

    @Override
    public Component getCellComponent(PreferenceRenderingEvent event) {
        return new JPanel();
    }
}

package org.cdc.potatomaker.preference.sections;

import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.preference.Preferences;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * e-mail: 3154934427@qq.com
 * 默认的设置模板
 *
 * @author cdc123
 * @classname DefaultPreferenceSectionModel
 * @date 2022/11/26 14:01
 */
@Open
public class DefaultPreferenceSectionModel implements PreferenceSectionModel{
    Preferences preferences = new Preferences();

    protected Class<? extends DefaultPreferenceSectionRender> dpr = DefaultPreferenceSectionRender.class;

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String[] getCells() {
        return new String[0];
    }

    @Override
    public PreferenceSectionRender getRender() {
        try {
            return dpr.getConstructor(DefaultPreferenceSectionModel.class).newInstance(this);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Preferences getPreferences(){
        return preferences;
    }
}

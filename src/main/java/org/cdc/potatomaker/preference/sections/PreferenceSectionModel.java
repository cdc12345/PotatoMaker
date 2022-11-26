package org.cdc.potatomaker.preference.sections;

import org.cdc.potatomaker.preference.Preferences;

/**
 * e-mail: 3154934427@qq.com
 * 首选项条目模板
 *
 * @author cdc123
 * @classname PreferenceModel
 * @date 2022/11/26 13:49
 */
public interface PreferenceSectionModel {

    String getName();

    String[] getCells();

    PreferenceSectionRender getRender();

    Preferences getPreferences();

    default boolean isHiddenControlPane(){
        return false;
    }
}

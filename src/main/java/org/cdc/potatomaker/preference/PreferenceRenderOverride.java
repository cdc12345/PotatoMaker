package org.cdc.potatomaker.preference;

import lombok.Setter;
import org.cdc.potatomaker.preference.render.PreferenceRender;

/**
 * e-mail: 3154934427@qq.com
 * 覆写管理类
 *
 * @author cdc123
 * @classname PreferenceRenderOverride
 * @date 2022/11/14 8:40
 */
public class PreferenceRenderOverride {
    @Setter
    private static PreferenceRender instance;

    public static PreferenceRender getInstance(){
        return null;
/*        if (instance == null) instance = new DefaultPreferenceRender();
        return instance;*/
    }


}

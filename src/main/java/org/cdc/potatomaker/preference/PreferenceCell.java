package org.cdc.potatomaker.preference;

import lombok.Data;
import org.cdc.potatomaker.preference.render.PreferenceCellRender;

/**
 * e-mail: 3154934427@qq.com
 * TODO
 *
 * @author cdc123
 * @classname PreferenceCell
 * @date 2022/11/14 8:17
 */
@Data
public class PreferenceCell {
    /**
     * 翻译程序会帮你按照键值 preferences.{sectionName}.{name}帮您翻译,而如果没有翻译.就会按照当前的名字
     */
    private String name;

    private PreferenceCellRender cellRender;

    public int getPriority(){
        return cellRender.getPriority();
    }
}

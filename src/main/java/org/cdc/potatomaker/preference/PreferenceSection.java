package org.cdc.potatomaker.preference;

import lombok.Getter;
import org.cdc.potatomaker.preference.render.PreferenceCellRender;
import org.cdc.potatomaker.preference.render.PreferenceSectionRender;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * e-mail: 3154934427@qq.com
 * 配置集合
 *
 * @author cdc123
 * @classname PreferenceSection
 * @date 2022/11/13 22:33
 */
public class PreferenceSection {
    @Getter
    private final String name;
    @Getter
    private final PreferenceSectionRender sectionRender;
    /**
     * 为保证顺序,使用 List
     */
    final Cells cells ;

    public PreferenceSection(String name){
        this(name,null);
    }

    public PreferenceSection(String name,PreferenceSectionRender render){
        this.name = name;
        this.sectionRender = render==null?new DefaultPreferenceSectionRender():render;
        cells = new Cells();
    }

    public boolean addPreferenceCell(PreferenceCell cell){
        if (cell.getPriority() < PreferenceCellRender.minPriority) return false;
        cells.add(cell);
        cells.sort(Comparator.comparingInt(PreferenceCell::getPriority));
        sectionRender.setCells(cells);
        return true;
    }

    public int getPriority(){
        return 0;
    }

    private static class Cells extends ArrayList<PreferenceCell> {
        public  Cells(){super();}
    }

}

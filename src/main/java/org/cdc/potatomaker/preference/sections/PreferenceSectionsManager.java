package org.cdc.potatomaker.preference.sections;

import java.util.concurrent.ConcurrentHashMap;

/**
 * e-mail: 3154934427@qq.com
 * 设置栏目管理类
 *
 * @author cdc123
 * @classname PreferenceSectionsManager
 * @date 2022/11/26 14:13
 */
public class PreferenceSectionsManager {
    private static final ConcurrentHashMap<String,PreferenceSectionModel> modelConcurrentHashMap = new ConcurrentHashMap<>();
    static {
        register(new DefaultPreferenceSectionModel(){
            @Override
            public String getName() {
                return "plugins";
            }

            @Override
            public boolean isHiddenControlPane() {
                return true;
            }
        });
    }
    public static void register(PreferenceSectionModel model){
        modelConcurrentHashMap.put(model.getName(),model);
    }

    public static void unregister(PreferenceSectionModel model){
        modelConcurrentHashMap.remove(model.getName());
    }

    public static void unregister(String name){
        modelConcurrentHashMap.remove(name);
    }

    public static String[] getSectionsName(){
        return modelConcurrentHashMap.keySet().toArray(new String[0]);
    }

    public static PreferenceSectionModel getSection(String name){
        return modelConcurrentHashMap.get(name);
    }
}

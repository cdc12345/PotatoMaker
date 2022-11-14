package org.cdc.potatomaker.preference;

import com.google.gson.Gson;
import org.cdc.potatomaker.preference.render.PreferenceRender;
import org.cdc.potatomaker.util.fold.UserFolderManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * e-mail: 3154934427@qq.com
 * 首选项管理类
 *
 * @author cdc123
 * @classname PreferenceManager
 * @date 2022/11/13 22:19
 */
public class PreferenceManager {
    private static final Object preLock = new Object();
    private static Preferences preferences;

    public static void loadPreferencesFromFile() throws FileNotFoundException {
        File preferenceFile = UserFolderManager.getInstance().getPreferenceFile();

        setPreferences(new Gson().fromJson(new FileReader(preferenceFile), Preferences.class));
    }

    public static void reloadPreferences() throws FileNotFoundException {
        loadPreferencesFromFile();
    }

    public static Preferences getPreferences(){
        //防止程序载入配置时读取,出现异常现象
        synchronized (preLock){
            return preferences;
        }
    }

    public static void setPreferences(Preferences pre){
        synchronized (preLock){
            preferences = pre;
        }
    }

    public static PreferenceRender getPreferenceRender(){
        return PreferenceRenderOverride.getInstance();
    }

    public static void storePreferences(){
        preferences = getPreferenceRender().storePreference();
    }


}

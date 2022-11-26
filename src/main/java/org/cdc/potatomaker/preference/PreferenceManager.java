package org.cdc.potatomaker.preference;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.preference.sections.PreferenceSectionsManager;
import org.cdc.potatomaker.util.fold.RuntimeWorkSpaceManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.cdc.potatomaker.util.ResourceManager.fromInputStream;

/**
 * e-mail: 3154934427@qq.com
 * 首选项管理类
 *
 * @author cdc123
 * @classname PreferenceManager
 * @date 2022/11/13 22:19
 */
public class PreferenceManager {
    private static final Logger LOGGER = LogManager.getLogger("Preference");
    private static Preferences preferences;

    public static void loadPreferencesFromFile() throws IOException {
        LOGGER.info("系统开始载入设置");
        setPreferences(new Gson().fromJson(fromInputStream(Objects.requireNonNullElse(RuntimeWorkSpaceManager
                .getInstance().getOuterResourceAsStream("config/preferences.json"),
                new ByteArrayInputStream("[]".getBytes(StandardCharsets.UTF_8)))), Preferences.class));
        LOGGER.info("设置载入成功");
    }

    public static void reloadPreferences() throws IOException {
        loadPreferencesFromFile();
    }

    public static Preferences getPreferences(){
        return preferences;
    }

    public static void setPreferences(Preferences pre){
        preferences = Objects.requireNonNullElseGet(pre, Preferences::new);
    }

    public static void storePreferences() throws IOException {
        var file = RuntimeWorkSpaceManager
                        .getInstance().getOuterResourceAsFile("config/preferences.json");
        FileUtils.writeStringToFile(file,new Gson().toJson(preferences),StandardCharsets.UTF_8,false);
    }

    public static String[] getSections(){
        return PreferenceSectionsManager.getSectionsName();
    }

}

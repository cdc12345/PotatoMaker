package org.cdc.potatomaker.util.locale;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.preference.PreferenceManager;
import org.cdc.potatomaker.resourcepack.themes.Theme;
import org.cdc.potatomaker.resourcepack.themes.ThemeManager;
import org.cdc.potatomaker.util.resource.UIRE;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static org.cdc.potatomaker.util.ResourceManager.fromInputStream;

/**
 * e-mail: 3154934427@qq.com
 * 国际化工具
 *
 * @author cdc123
 * @classname GlobalUtil
 * @date 2022/11/14 20:21
 */
public class GlobalUtil {
    private static final Logger LOGGER = LogManager.getLogger("GlobalUtil");

    private static final String defaultLang = "zh_cn";

    private static GlobalUtil instance;

    public static GlobalUtil getInstance() {
        if (instance == null) {
            instance = new GlobalUtil();
        }
        return instance;
    }

    private GlobalUtil() {
        //首先载入默认语言文件
        LanguageMap languageMap = new LanguageMap();
        for (InputStream inputStream:UIRE.getInstance().
                getResourcesByKeyWord("lang/"+defaultLang+".json")){
            var map = new Gson().fromJson(fromInputStream(inputStream),LanguageMap.class);
            languageMap.putAll(map);
        }
        lang.put(defaultLang,languageMap);
        //然后载入当前配置的语言文件,排除默认语言
        var lang1 = (String)PreferenceManager.getPreferences().getOrDefault("globalLang","zh_cn");
        if (defaultLang.equals(lang1)) {
            var current = "lang/" + lang1 + ".json";
            LanguageMap languageMap1 = new LanguageMap();
            for (InputStream inputStream:UIRE.getInstance().getResourcesByKeyWord("lang/"+lang1+".json")){
                var map = new Gson().fromJson(fromInputStream(inputStream), LanguageMap.class);
                languageMap1.putAll(map);
            }
            lang.put(lang1, languageMap1);
        }
        setCurrentLang(lang1);
        LOGGER.info("当前语言"+lang1);
    }
    
    private final ConcurrentHashMap<String,LanguageMap> lang = new ConcurrentHashMap<>();

    @Getter
    private String currentLang;

    public void setCurrentLang(String currentLang){
        this.currentLang = currentLang;
    }

    public void addLanguage(String language,InputStream inputStream){
        lang.put(language,new Gson().fromJson(new InputStreamReader(inputStream),LanguageMap.class));
    }


    public String getLangValue(String key,Object... objects){
        return MessageFormat.format(lang.get(getCurrentLang()).getOrDefault(key,key),objects);
    }

    //动态修改语言
    public void setLang(String key,String value){
        this.setLang(currentLang,key,value);
    }

    public void setLang(String lang,String key,String value){
        this.lang.get(lang).put(key,value);
    }

    public static class LanguageMap extends HashMap<String,String>{}

    /**
     * 为了兼容mcr
     * @param key 键值
     * @return 翻译
     */
    public String t(String key,Object... args){
        return getLangValue(key,args);
    }


    public JLabel label(String key,Object... args){
        var label = new JLabel(t(key,args));
        label.setFont(UIManager.getFont(ThemeManager.commonFont));
        label.setForeground(Theme.getInstance().getColorScheme().getForegroundColor());
        label.setOpaque(false);
        return label;
    }
}

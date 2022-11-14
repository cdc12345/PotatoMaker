package org.cdc.potatomaker.util.locale;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * e-mail: 3154934427@qq.com
 * 国际化工具
 *
 * @author cdc123
 * @classname GlobalUtil
 * @date 2022/11/14 20:21
 */
public class GlobalUtil {
    private final ConcurrentHashMap<String,LanguageMap> lang = new ConcurrentHashMap<>();

    private String currentLang;

    public void addLanguage(String language,InputStream inputStream){
        lang.put(language,new Gson().fromJson(new InputStreamReader(inputStream),LanguageMap.class));
    }

    public void setCurrentLang(String lang){
        currentLang = lang;
    }

    public String getLangValue(String key){
        return lang.get(currentLang).get(key);
    }

    public String getLangValue(String key,Object... objects){
        return MessageFormat.format(getLangValue(key),objects);
    }

    //动态修改语言
    public void setLang(String key,String value){
        this.setLang(currentLang,key,value);
    }

    public void setLang(String lang,String key,String value){
        this.lang.get(lang).put(key,value);
    }

    public static class LanguageMap extends HashMap<String,String>{}

}

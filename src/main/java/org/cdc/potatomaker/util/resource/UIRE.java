package org.cdc.potatomaker.util.resource;

import org.cdc.potatomaker.util.ResourceManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * e-mail: 3154934427@qq.com
 * 仿照mcreator的资源管理类的管理类
 *
 * @author cdc123
 * @classname UIRE
 * @date 2022/11/15 10:11
 */
public class UIRE {
    private static UIRE instance;

    public static UIRE getInstance() {
        if (instance == null) {
            instance = new UIRE();
        }
        return instance;
    }
    private ConcurrentHashMap<String,InputStream> resources = new ConcurrentHashMap<>();
    private UIRE() {
        resources = new ConcurrentHashMap<>();
    }
    public void addResource(String name,InputStream inputStream){
        resources.put(name,inputStream);
    }

    public InputStream getResource(Pattern name){
        return resources.entrySet().stream().filter(a->name.matcher(a.getKey()).find()).map(Map.Entry::getValue)
                .findFirst().orElse(ResourceManager.getInnerResourceAsStream(name.pattern()));
    }
}

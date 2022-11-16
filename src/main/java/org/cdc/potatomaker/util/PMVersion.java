package org.cdc.potatomaker.util;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * e-mail: 3154934427@qq.com
 * 版本工具
 *
 * @author cdc123
 * @classname PMVersion
 * @date 2022/11/15 10:00
 */
public class PMVersion {
    /**
     * 简单获取内部版本
     * @param version
     * @return
     */
    public static int getInnerVersion(@NotNull String version){
        if ( version.contains(".")){
            AtomicInteger innerVersion = new AtomicInteger();
            Arrays.stream(version.split("\\.")).forEach(a-> innerVersion.addAndGet(Integer.parseInt(a)));
            return innerVersion.get();
        }
        return -1;
    }

    private static PMVersion instance;

    public static PMVersion getInstance() {
        if (instance == null) {
            instance = new PMVersion();
        }
        return instance;
    }

    @Getter
    private final String version;//大写,感觉好难看.
    @Getter
    private final int innerVersion;

    private PMVersion() {
        Properties properties = new Properties();

        try {
            properties.load(this.getClass().getResourceAsStream("/pm.properties"));
        } catch (IOException e) {
            properties.setProperty("version",System.getProperty("potatoMaker.version","2022.11"));
            properties.setProperty("innerVersion",System.getProperty("potatoMaker.innerVersion","1"));
        }

        version = properties.getProperty("version");
        innerVersion = Integer.parseInt(properties.getProperty("innerVersion"));
    }
}

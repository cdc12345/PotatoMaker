package org.cdc.potatomaker.util;

import lombok.Getter;

import java.io.IOException;
import java.util.Properties;

/**
 * e-mail: 3154934427@qq.com
 * TODO
 *
 * @author cdc123
 * @classname PMVersion
 * @date 2022/11/15 10:00
 */
public class PMVersion {
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

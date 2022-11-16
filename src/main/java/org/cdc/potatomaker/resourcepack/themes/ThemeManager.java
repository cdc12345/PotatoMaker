package org.cdc.potatomaker.resourcepack.themes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.events.Events;
import org.cdc.potatomaker.events.ThemeInitEvent;
import org.cdc.potatomaker.util.resource.UIRE;

import javax.swing.*;
import java.util.regex.Pattern;


/**
 * e-mail: 3154934427@qq.com
 * 主题设置
 * pm和mcr的主题定义有点不同,pm是基于资源包来进行的主题设置,意味着可以覆写.
 * @author cdc123
 * @classname ThemeManager
 * @date 2022/11/15 16:37
 */
public class ThemeManager {
    private static final Logger LOG = LogManager.getLogger("ThemeManager");

    public static String icon = "pm.icon";

    public static void initTheme(){
        Events.registerListener(ThemeInitEvent.class,event -> {
            LOG.info("正在载入ICON");
            UIManager.put(icon,event.getUIRE().getResource(Pattern.compile("icon\\.png")));
            LOG.info("载入Theme");

        });

        Events.invokeEvent(new ThemeInitEvent(new ThemeManager(),UIRE.getInstance()));
    }
}

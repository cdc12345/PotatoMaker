package org.cdc.potatomaker.resourcepack.themes;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.events.Events;
import org.cdc.potatomaker.events.PMEvents;
import org.cdc.potatomaker.events.ThemeInitEvent;
import org.cdc.potatomaker.ui.laf.MCreatorLookAndFeel;
import org.cdc.potatomaker.util.resource.UIRE;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.regex.Pattern;

import static org.cdc.potatomaker.util.ResourceManager.fromInputStream;


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

    public static String icon = "pm.icon.png";
    public static String commonFont = "pm.normal.ttf";
    public static String colorScheme = "pm.colorScheme";

    public static void initTheme(){
        PMEvents.registerListener(ThemeInitEvent.class,"Theme-Init", event -> {
            LOG.info("正在载入默认的相关资源");
            UIRE uire = event.getUIRE();
            try {
                UIManager.put(icon,ImageIO.read(uire.getResourceByKeyWord("images/icon.png")));
                UIManager.put(commonFont, Font.createFont(Font.TRUETYPE_FONT,uire.getResourceByKeyWord("fonts/normal.ttf")));
                UIManager.put(colorScheme,new Gson().fromJson(fromInputStream(uire.getResourceByKeyWord("colorScheme.json")),
                    ColorScheme.class));
            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
            }
            LOG.info("设置观感...");
            try {
                UIManager.setLookAndFeel(new MCreatorLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        });

        Events.invokeEvent(new ThemeInitEvent(new ThemeManager(),UIRE.getInstance()));
    }
}

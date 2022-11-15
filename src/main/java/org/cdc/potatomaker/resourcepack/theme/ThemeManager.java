package org.cdc.potatomaker.resourcepack.theme;

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
    public static void initTheme(){
        UIManager.put("pm.icon", UIRE.getInstance().getResource(Pattern.compile("icon\\.png")));
    }
}

package org.cdc.pm.tests;

import org.cdc.potatomaker.Launcher;
import org.cdc.potatomaker.exception.DefinedException;
import org.cdc.potatomaker.plugin.loader.PMPluginLoader;
import org.cdc.potatomaker.ui.dialogs.UserFolderSelector;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * e-mail: 3154934427@qq.com
 * 插件载入测试
 *
 * @author cdc123
 * @classname PluginTest
 * @date 2022/11/14 15:51
 */
public class PluginTest {
    @Test
    public void PluginLoadTest() throws DefinedException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Launcher.main(null);
        UserFolderSelector.userFolder = System.getProperty("user.dir");
        PMPluginLoader.getInstance().loadPlugins();
    }
}

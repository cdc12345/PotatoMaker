package org.cdc.potatomaker.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.events.WithoutArgsStartingEvent;
import org.cdc.potatomaker.events.Events;
import org.cdc.potatomaker.events.WithArgsStartingEvent;
import org.cdc.potatomaker.exception.DefinedException;
import org.cdc.potatomaker.exception.PluginEnabledException;
import org.cdc.potatomaker.plugin.loader.PMPluginLoader;
import org.cdc.potatomaker.preference.PreferenceManager;
import org.cdc.potatomaker.preference.Preferences;
import org.cdc.potatomaker.resourcepack.PMResourcePackLoader;
import org.cdc.potatomaker.resourcepack.theme.ThemeManager;
import org.cdc.potatomaker.ui.SplashScreen;
import org.cdc.potatomaker.ui.dialogs.UserFolderSelector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * e-mail: 3154934427@qq.com
 * TODO
 *
 * @author cdc123
 * @classname PotatoMakerApplication
 * @date 2022/11/15 12:49
 */
public class PotatoMakerApplication {
    private static Logger LOG = LogManager.getLogger("PM");

    private static PotatoMakerApplication instance;

    public static PotatoMakerApplication createInstance(String[] args) throws DefinedException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if (instance == null) {
            instance = new PotatoMakerApplication(Arrays.asList(args));
        }
        return instance;
    }

    public static PotatoMakerApplication getInstance(){
        return instance;
    }

    private PotatoMakerApplication(List<String> args) throws DefinedException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        SplashScreen splashDialog = new SplashScreen();
        splashDialog.setVisible(true);

        splashDialog.setProgress(10,"载入设置中..");
        try {
            PreferenceManager.loadPreferencesFromFile();
        } catch (IOException ignore){
            PreferenceManager.setPreferences(new Preferences());
            PreferenceManager.storePreferences();
        }

        splashDialog.setProgress(15,"选择相应的用户目录..");
        LOG.info("选择用户目录");
        if (PreferenceManager.getPreferences().getOrDefault(UserFolderSelector.notAskKey,true).toString().equals("false")||
                UserFolderSelector.getInstance().userFolder == null) {
            UserFolderSelector.showUserFolderSelector();
        }

        splashDialog.setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            LOG.info("正在关闭系统");
            PMPluginLoader.getInstance().unloadPlugins();
            PreferenceManager.storePreferences();
        }));

        LOG.info("正在载入插件中");
        splashDialog.setProgress(20,"载入插件中..");
        PMPluginLoader.getInstance().loadPlugins();

        LOG.info("正在载入资源包");
        splashDialog.setProgress(40,"载入资源包中..");
        PMResourcePackLoader.getInstance().loadPacks();

        LOG.info("正在启用插件");
        splashDialog.setProgress(60,"启用插件中..");
        try {
            PMPluginLoader.getInstance().enablePlugins();
        } catch (PluginEnabledException ignore) {}

        LOG.info("正在启用资源包");
        splashDialog.setProgress(70,"启用资源包中..");
        PMResourcePackLoader.getInstance().enablePacks();

        LOG.info("正在启用主题");
        ThemeManager.initTheme();

        LOG.info("正在执行插件操作");
        splashDialog.setProgress(80,"执行插件事件操作中..");
        if (args.size() != 0){
            Events.invokeEvent(new WithArgsStartingEvent(this,args,splashDialog));
        } else {
            Events.invokeEvent(new WithoutArgsStartingEvent(this,splashDialog));
        }

    }
}

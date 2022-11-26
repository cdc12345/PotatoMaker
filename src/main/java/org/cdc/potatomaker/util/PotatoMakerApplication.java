package org.cdc.potatomaker.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.events.application.ApplicationExitEvent;
import org.cdc.potatomaker.events.application.ApplicationInitEvent;
import org.cdc.potatomaker.events.Events;
import org.cdc.potatomaker.events.PMEvents;
import org.cdc.potatomaker.exception.DefinedException;
import org.cdc.potatomaker.exception.PluginEnabledException;
import org.cdc.potatomaker.plugin.loader.PMPluginLoader;
import org.cdc.potatomaker.preference.PreferenceManager;
import org.cdc.potatomaker.preference.Preferences;
import org.cdc.potatomaker.resourcepack.PMResourcePackLoader;
import org.cdc.potatomaker.resourcepack.themes.ThemeManager;
import org.cdc.potatomaker.ui.SplashScreen;
import org.cdc.potatomaker.ui.dialogs.UserFolderSelector;
import org.cdc.potatomaker.ui.workspace.selector.WorkspaceSelector;
import org.cdc.potatomaker.util.locale.GlobalUtil;
import org.cdc.potatomaker.workspace.Workspace;
import org.cdc.potatomaker.workspace.WorkspaceManager;
import org.cdc.potatomaker.workspace.type.WorkspaceModelManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * e-mail: 3154934427@qq.com
 * PM软件运行类
 *
 * @author cdc123
 * @classname PotatoMakerApplication
 * @date 2022/11/15 12:49
 */
public class PotatoMakerApplication {
    private static Logger LOG = LogManager.getLogger("PM");

    private static PotatoMakerApplication instance;

    public static PotatoMakerApplication createInstance(String[] args) throws DefinedException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException, IOException {
        if (instance == null) {
            instance = new PotatoMakerApplication(Arrays.asList(args));
        }
        return instance;
    }

    public static PotatoMakerApplication getInstance(){
        return instance;
    }

    private PotatoMakerApplication(List<String> args) throws DefinedException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException, IOException {
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
        LOG.info("用户选择的用户目录为:"+UserFolderSelector.getInstance().userFolder);

        splashDialog.setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread(this::exitApplication));

        PMEvents.registerListener(ApplicationExitEvent.class,"PM-Exit", a->{
            LOG.info("正在禁用所有插件");
            PMPluginLoader.getInstance().unloadPlugins();
        });

        LOG.info("正在载入插件中");
        splashDialog.setProgress(20,"载入插件中..");
        PMPluginLoader.getInstance().loadPlugins();

        LOG.info("正在载入资源包");
        splashDialog.setProgress(40,"载入资源包中..");
        PMResourcePackLoader.getInstance().loadPacks();

        LOG.info("正在启用插件");
        splashDialog.setProgress(50,"启用插件中..");
        try {
            PMPluginLoader.getInstance().enableAllPlugins();
        } catch (PluginEnabledException ignore) {}

        LOG.info("正在启用资源包");
        splashDialog.setProgress(60,"启用资源包中..");
        PMResourcePackLoader.getInstance().enablePacks();

        LOG.info("正在启用语言");
        splashDialog.setProgress(70,"启用语言中..");
        GlobalUtil.getInstance();

        LOG.info("正在启用主题");
        ThemeManager.initTheme();

        LOG.info("正在执行插件操作");
        splashDialog.setProgress(80,"执行插件事件操作中..");
        Events.invokeEvent(new ApplicationInitEvent(this,args));

        LOG.info("唤起主界面");
        splashDialog.setProgress(90,"唤起主界面");
        if (args.size() == 1){
            openWorkspace(new File(args.get(0)));
        } else new WorkspaceSelector().setVisible(true);

        splashDialog.setVisible(false);

    }

    public void exitApplication(){
        LOG.info("Do some actions to exit");
        Events.invokeEvent(new ApplicationExitEvent(this));
        try {
            PreferenceManager.storePreferences();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openWorkspace(File workspaceFolder){

    }

    public void createWorkspace(WorkspaceModelManager.WorkspaceModelPack model, File workspaceFolder){
        Workspace workspace = WorkspaceManager.getOrCreateWorkspace(workspaceFolder);
        workspace.getWorkspaceConfig().setType(model.type());
        workspace.getWorkspaceConfig().setFork(model.fork());
        workspace.getWorkspaceConfig().setPlugins(PMPluginLoader.getInstance().getPlugins().keySet().toArray(new String[0]));
    }
}

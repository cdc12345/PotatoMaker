package org.cdc.potatomaker.util.fold;

import org.cdc.potatomaker.ui.dialogs.UserFolderSelector;
import org.cdc.potatomaker.util.ResourceManager;

import java.io.File;

/**
 * e-mail: 3154934427@qq.com
 * 用户文件夹管理类
 *
 * @author cdc123
 * @classname UserFolderManager
 * @date 2022/11/13 21:52
 */
public class UserFolderManager extends ResourceManager {
    private static UserFolderManager instance;

    public static UserFolderManager getInstance() {
        if (instance == null) {
            instance = new UserFolderManager(UserFolderSelector.userFolder);
        }
        return instance;
    }

    UserFolderManager(String parent) {
        super(parent);
        initUserFolder();
    }

    public void initUserFolder() {
        getGradleCacheFolder().mkdirs();

        getCacheFolder().mkdirs();

        getPluginsFolder().mkdirs();

        getBackgroundsFolder().mkdirs();
    }

    public File getGradleCacheFolder(){
        return getOuterResourceAsFile(".gradle");
    }

    public File getCacheFolder(){
        return getOuterResourceAsFile(".cache");
    }

    public File getPluginsFolder(){
        return getOuterResourceAsFile("plugins");
    }

    public File getBackgroundsFolder(){
        return getOuterResourceAsFile("backgrounds");
    }

    public File getPreferenceFile(){
        return getOuterResourceAsFile("preferences");
    }
}

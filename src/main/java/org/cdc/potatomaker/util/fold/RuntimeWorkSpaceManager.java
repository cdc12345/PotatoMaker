package org.cdc.potatomaker.util.fold;

import org.cdc.potatomaker.util.ResourceManager;

import java.io.File;

/**
 * e-mail: 3154934427@qq.com
 * 运行空间管理类
 *
 * @author cdc123
 * @classname RuntimeWorkSpaceManager
 * @date 2022/11/13 21:44
 */
public class RuntimeWorkSpaceManager extends ResourceManager {
    private static RuntimeWorkSpaceManager instance;

    public static RuntimeWorkSpaceManager getInstance() {
        if (instance == null) {
            instance = new RuntimeWorkSpaceManager();
        }
        return instance;
    }
    
    private RuntimeWorkSpaceManager() {
        super(new File(System.getProperty("user.dir")));
        initFolders();
    }

    public void initFolders() {

        getPluginFolder().mkdirs();

        getResourcePackFolder().mkdirs();
    }

    public File getPluginFolder(){
        return getOuterResourceAsFile("plugins");
    }

    /**
     * 资源包路径,用来替换资源,类似于主题
     * @return 资源包路径
     */
    public File getResourcePackFolder(){
        return getOuterResourceAsFile("resourcepacks");
    }

    public File getPluginsFolder(){
        return getOuterResourceAsFile("plugins");
    }
}

package org.cdc.potatomaker.plugin.loader;

import org.cdc.potatomaker.annotation.Open;
import org.cdc.potatomaker.util.ResourceManager;

import java.io.File;

/**
 * e-mail: 3154934427@qq.com
 * 插件必须实现的类
 *
 * @author cdc123
 * @classname JavaPlugin
 * @date 2022/11/13 21:34
 */
@Open
public abstract class AbstractPlugin {

    public static AbstractPlugin getPlugin(String name){
        return PMPluginLoader.getInstance().getPlugins().get(name).getPluginInstance();
    }

    private PluginLoader pluginLoader;
    /**
     * 这个方法只有特定的插件类型才能覆写,其他类型的插件覆写了不会调用
     */
    public void onLoad(){

    }
    public abstract void onEnable();

    public abstract void onDisable();

    void inject(PluginLoader pluginLoader){
        this.pluginLoader = pluginLoader;
    }

    protected PluginLoader getPluginLoader(){
        return pluginLoader;
    }

    protected ResourceManager getDataFolder(){
        return new ResourceManager(getPluginLoader().getPluginDataFolder());
    }

}

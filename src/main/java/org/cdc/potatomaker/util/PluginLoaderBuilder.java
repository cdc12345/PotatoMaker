package org.cdc.potatomaker.util;

import org.apache.logging.log4j.LogManager;
import org.cdc.potatomaker.exception.PluginExistException;
import org.cdc.potatomaker.plugin.PluginInfo;
import org.cdc.potatomaker.plugin.loader.ModulePluginLoader;
import org.cdc.potatomaker.plugin.loader.PMPluginLoader;
import org.cdc.potatomaker.plugin.loader.PluginLoader;

import java.io.File;

/**
 * e-mail: 3154934427@qq.com
 * 插件Loader(我也不知道咋翻)构建器
 *
 * @author cdc123
 * @classname PluginLoaderBuilder
 * @date 2022/11/14 15:12
 */
public class PluginLoaderBuilder{
    private final PluginLoader pluginLoader;
    public PluginLoaderBuilder(){
        pluginLoader = new PluginLoader();
    }

    public PluginLoaderBuilder setPluginPath(File pluginPath){
        pluginLoader.setPluginPath(pluginPath);
        return this;
    }

    public PluginLoaderBuilder setPluginInfo(PluginInfo pluginInfo) throws PluginExistException {
        if (PMPluginLoader.getInstance().getPlugins().containsKey(pluginInfo.getName())){
            throw new PluginExistException("插件:"+pluginInfo.getName()+"已经存在");
        }
        pluginLoader.setPluginInfo(pluginInfo);
        return this;
    }


    public PluginLoaderBuilder setLoaderType(String loaderType){
        pluginLoader.setLoaderType(loaderType);
        return this;
    }

    public PluginLoaderBuilder setPluginClassLoader(ModulePluginLoader pluginLoader){
        this.pluginLoader.setClassLoader(pluginLoader);
        return this;
    }

    public PluginLoaderBuilder setLogger(){
        if (pluginLoader.getPluginInfo() != null) pluginLoader.setLogger(LogManager.getLogger(pluginLoader.getPluginInfo().getName()));
        return this;
    }

    public PluginLoader build(){
        return pluginLoader;
    }

}

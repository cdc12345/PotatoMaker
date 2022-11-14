package org.cdc.potatomaker.plugin.loader;

import lombok.Data;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.plugin.PluginInfo;

import java.io.File;

/**
 * e-mail: 3154934427@qq.com
 * 插件载入器
 *
 * @author cdc123
 * @classname PluginLoader
 * @date 2022/11/13 21:29
 */
@Data
public class PluginLoader {
    /**
     * 插件实例
     */
    private AbstractPlugin pluginInstance;
    /**
     * 插件信息实例
     */
    private PluginInfo pluginInfo;
    /**
     * 插件语言类型
     */
    private String loaderType;
    /**
     * 载入的动态类加载器
     */
    private ModulePluginLoader classLoader;
    /**
     * 插件路径
     */
    private File pluginPath;
    /**
     *插件数据文件夹位置
     */
    private File pluginDataFolder;
    /**
     * 插件Logger
     */
    private Logger logger;

}

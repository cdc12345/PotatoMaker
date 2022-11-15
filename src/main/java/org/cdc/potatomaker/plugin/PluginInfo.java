package org.cdc.potatomaker.plugin;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.cdc.potatomaker.plugin.loader.PluginLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * e-mail: 3154934427@qq.com
 * 插件信息类,只能读取不能写入
 *
 * @author cdc123
 * @classname Plugin
 * @date 2022/11/13 21:05
 */
@Data
public class PluginInfo {
    /**
     * 插件名称
     */
    @NotNull
    private String name;
    /**
     * 展示版本,此信息展示给用户
     */
    @NotNull
    private String version;
    /**
     * 内部版本,用于对比以进行版本更新
     */
    private int innerVersion = -1;
    /**
     * 插件主类
     *
    */
    private String main;
    /**
     * 作者列表
     */
    private String[] authors;
    /**
     * 插件类型
     */
    private PluginType[] types = new PluginType[]{PluginType.Unknown};
    /**
     * 依赖
     */
    private String[] depends;
    /**
     * 插件实例化优先级
     */
    private int priority = -1;
    /**
     * 插件最小支持pm版本,请填int
     */
    private int minVersion = 1;

    /**
     * 得到内部版本
     * @return 内部版本
     */
    public int getInnerVersion(){
        //如果含有.,我们推测版本号可能类似于 1.0.1,系统会相加这三个数字来获取innerVersion
        if (innerVersion < 0 && version.contains(".")){
            AtomicInteger innerVersion = new AtomicInteger();
            Arrays.stream(version.split("\\.")).forEach(a-> innerVersion.addAndGet(Integer.parseInt(a)));
            return innerVersion.get();
        }
        return innerVersion;
    }
}

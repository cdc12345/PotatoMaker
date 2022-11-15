package org.cdc.potatomaker.plugin.loader;

import org.cdc.potatomaker.annotation.Open;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/**
 * e-mail: 3154934427@qq.com
 * 模块化的插件载入器,此插件载入器针对依赖分离问题进行了修复
 *
 * @author cdc123
 * @classname ModulePluginLoader
 * @date 2022/11/14 14:23
 */
@Open
public class ModulePluginLoader extends URLClassLoader {
    private final String limitClassPackagePrefix ;
    private final ClassLoader originClassLoader;
    public ModulePluginLoader(File pluginPath,ClassLoader origin) throws MalformedURLException {
        this(pluginPath,origin,"org.cdc.potatomaker");
    }
    public ModulePluginLoader(File pluginPath,ClassLoader origin,String limitClassPackage) throws MalformedURLException {
        super(new URL[]{pluginPath.toURI().toURL()},null);
        this.originClassLoader = origin;
        this.limitClassPackagePrefix = limitClassPackage;
    }

    private final ArrayList<ModulePluginLoader> dependencies = new ArrayList<>();

    public void addDependency(ModulePluginLoader pluginLoader){
        dependencies.add(pluginLoader);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> result = null;
        //从我这找
        try{
            result = originClassLoader.loadClass(name);
            if (result.getPackage().getName().startsWith(limitClassPackagePrefix)
                    &&!result.isAnnotation()&&!result.isAnnotationPresent(Open.class)){
                result = null;
            }
        } catch (Exception ignore){
        }

        //从依赖中寻找
        for (ModulePluginLoader pluginLoader : dependencies) {
            try {
                result = pluginLoader.loadClass(name, resolve);
            } catch (ClassNotFoundException ignore) {
            }
        }
        //从自己那找
        try {
            result = super.loadClass(name, resolve);
        } catch (ClassNotFoundException ignore){}
        if (result == null) throw new ClassNotFoundException("can't find class:"+name);
        return result;
    }
}

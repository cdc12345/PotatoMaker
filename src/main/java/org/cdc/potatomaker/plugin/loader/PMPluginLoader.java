package org.cdc.potatomaker.plugin.loader;

import com.google.gson.Gson;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.exception.DefinedException;
import org.cdc.potatomaker.exception.PluginEnabledException;
import org.cdc.potatomaker.exception.PluginExistException;
import org.cdc.potatomaker.plugin.PluginInfo;
import org.cdc.potatomaker.plugin.PluginType;
import org.cdc.potatomaker.preference.PreferenceManager;
import org.cdc.potatomaker.preference.Preferences;
import org.cdc.potatomaker.util.PMVersion;
import org.cdc.potatomaker.util.PluginLoaderBuilder;
import org.cdc.potatomaker.util.fold.RuntimeWorkSpaceManager;
import org.cdc.potatomaker.util.fold.UserFolderManager;
import org.cdc.potatomaker.util.resource.UIRE;

import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.jar.JarFile;

/**
 * e-mail: 3154934427@qq.com
 * PM载入插件时用的类
 *
 * @author cdc123
 * @classname PMPluginLoader
 * @date 2022/11/13 21:41
 */
public class PMPluginLoader {
    private static final Logger LOG = LogManager.getLogger("PMPluginLoader");

    private static PMPluginLoader instance;

    public static PMPluginLoader getInstance() {
        if (instance == null) {
            instance = new PMPluginLoader();
        }
        return instance;
    }

    /**
     * 运行目录下的插件
     */
    private final File firstPluginPath;
    /**
     * 用户目录下的插件
     */
    private final File secondPluginPath;
    /**
     * 插件缓存
     */
    @Getter
    private final HashMap<String,PluginLoader> plugins;

    /**
     * 无效的插件缓存
     */
    private final HashMap<String,PluginLoader> invalidatePlugins;

    private PMPluginLoader() {
        firstPluginPath = RuntimeWorkSpaceManager.getInstance().getPluginFolder();
        LOG.info("插件目录1:"+firstPluginPath);
        secondPluginPath = UserFolderManager.getInstance().getPluginsFolder();
        LOG.info("插件目录2:"+secondPluginPath);
        plugins = new HashMap<>();
        invalidatePlugins = new HashMap<>();
    }

    public void loadPlugins() throws DefinedException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        LOG.info("开始载入插件");
        Preferences preferences = PreferenceManager.getPreferences();
        preferences.computeIfAbsent("hidden.pluginsEnabled", k -> new ArrayList<>());
        for (File a:listPlugins()){
            try {
                var loader = loadPlugin(a);
                if (checkPlugin(loader)) {
                    invalidatePlugins.put(loader.getPluginInfo().getName(),loader);
                    continue;
                }
                plugins.put(loader.getPluginInfo().getName(),loader);
                LOG.info("PM成功载入插件"+loader.getPluginInfo().getName()+" ,插件目录:"+a);
            } catch (MalformedURLException e) {
                LOG.warn("无法载入插件:" + a);
            } catch (PluginExistException e) {
                LOG.warn("无法载入重名插件:" + a);
            }catch (NullPointerException e){
                LOG.warn("无法载入插件:"+a+", 可能原因:缺少plugin.json");
            } catch (Exception e){
                e.printStackTrace();
                LOG.warn("因为未知的原因无法载入插件:"+a);
            }
        }
        List<PluginLoader> loaders = plugins.values().stream()
                .sorted(Comparator.comparingInt(value -> value.getPluginInfo().getPriority())).toList();
        for (PluginLoader a:loaders){
            PluginInfo info = a.getPluginInfo();
            //依赖处理
            if (info.getDepends()!=null&&info.getDepends().length!=0){
                for (String dep:info.getDepends()){                    var dependLoader = plugins.get(dep);
                    if (dependLoader == null) throw new DefinedException("无法找到插件"+info.getName()+"的依赖"+dep);
                    a.getClassLoader().addDependency(dependLoader.getClassLoader());
                }
            }
            //软依赖
            if (info.getSoftDepends()!=null&&info.getSoftDepends().length!=0){
                for (String dep:info.getSoftDepends()){
                    var dependLoader = plugins.get(dep);
                    if (dependLoader != null)
                        a.getClassLoader().addDependency(dependLoader.getClassLoader());
                }
            }
            LOG.info("插件"+a.getPluginInfo().getName()+"依赖梳理完毕");
            //插件实例处理,致敬Java服务器插件的模式
            if (info.getMain() != null) {
                try {
                    Class<?> mainClass = a.getClassLoader().loadClass(info.getMain());
                    var abstractPluginClass = AbstractPlugin.class;
                    var filed = abstractPluginClass.getDeclaredField("pluginLoader");
                    filed.setAccessible(true);
                    AbstractPlugin mainIn = (AbstractPlugin) mainClass.getConstructor(new Class[0]).newInstance();
                    filed.set(mainIn,a);
                    a.setPluginInstance(mainIn);
                    if (Arrays.asList(info.getTypes()).contains(PluginType.VanillaAddon)) {
                        mainIn.onLoad();
                    }
                } catch (ClassNotFoundException i) {
                    i.printStackTrace();
                    throw new DefinedException("无法载入插件" + info.getName() + "的类,请开发者确定是否有误,开发者为"+Arrays.toString(info.getAuthors()));
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        ArrayList<String> enabled = (ArrayList<String>) preferences.get("hidden.pluginsEnabled");
        for (String plugin:enabled){
            plugins.get(plugin).setEnable(true);
        }
    }

    public List<File> listPlugins(){
        FilenameFilter filenameFilter = (a,b)->"jar".
                equals(FilenameUtils.getExtension(b));
        ArrayList<File> plugins = new ArrayList<>(List.of(
                Objects.requireNonNull(firstPluginPath.listFiles(filenameFilter))));
        plugins.addAll(Arrays.asList(Objects.requireNonNull(secondPluginPath.listFiles(filenameFilter))));
        LOG.info("已找到"+plugins.size()+"个插件");
        return plugins;
    }

    private PluginLoader loadPlugin(File plugin) throws MalformedURLException, PluginExistException {
        ModulePluginLoader modulePluginLoader = new ModulePluginLoader(plugin,Thread.currentThread().getContextClassLoader());
        return new PluginLoaderBuilder().setPluginPath(plugin).setLoaderType("java").setPluginClassLoader(modulePluginLoader)
                .setPluginInfo(loadPluginInfo(modulePluginLoader)).setLogger().build();
    }

    private PluginInfo loadPluginInfo(ModulePluginLoader pluginLoader){
        return new Gson().fromJson(new InputStreamReader(Objects.
                requireNonNull(pluginLoader.getResourceAsStream("plugin.json"))),PluginInfo.class);
    }

    private boolean checkPlugin(PluginLoader loader){
        if (!loader.getPluginInfo().getName().matches("[a-zA-Z_]+")){
            String message = "插件"+loader.getPluginPath()+"的名字存在非法字符,插件名称只能使用英文和_,已经忽略";
            LOG.warn(message);
            JOptionPane.showMessageDialog(null,message);
            return true;
        }
        if (loader.getPluginInfo().getMinVersion() > PMVersion.getInstance().getInnerVersion()){
            String message = "插件"+loader.getPluginPath()+"并不适用于当前版本,已经忽略";
            LOG.warn(message);
            JOptionPane.showMessageDialog(null,message);
            return true;
        }
        return false;
    }

    public void unloadPlugins(){
        plugins.values().forEach(a->{
            if (a.getPluginInstance() != null && a.isEnable())
                a.getPluginInstance().onDisable();
        }
        );
    }
    /**
     * 只有被启用的插件才能获得数据目录
     * @param pluginLoader 插件载入器
     * @throws PluginEnabledException 插件已经启用时会报错
     */
    public void enablePlugin(PluginLoader pluginLoader) throws PluginEnabledException, IOException {
        String pluginName = pluginLoader.getPluginInfo().getName();
        LOG.info("正在启用插件"+pluginName);
        if (pluginLoader.isEnabled()){
            throw new PluginEnabledException("can't enable plugin:"+pluginName+",because it is enabled");
        }
        //载入资源文件
        JarFile jarFile = new JarFile(pluginLoader.getPluginPath());
        jarFile.stream().forEach(jarEntry ->{
            if (jarEntry.getName().startsWith("assets")){
                try {
                    UIRE.getInstance().addResource(jarEntry.getName(),jarFile.getInputStream(jarEntry));
                } catch (IOException ignore) {
                    LOG.info("无效资源:位于插件"+pluginLoader.getPluginInfo().getName()+"的"+jarEntry.getName());
                }
            }
        });
        jarFile.close();

        if (pluginLoader.getPluginInstance() != null){
            pluginLoader.getPluginInstance().onEnable();
        }
        pluginLoader.setPluginDataFolder(new File(UserFolderManager.getInstance().getCacheFolder(),pluginName));
        pluginLoader.setEnabled(true);
    }

    public void enableAllPlugins() throws PluginEnabledException {
        Preferences preferences = PreferenceManager.getPreferences();
        ArrayList<String> enabled = (ArrayList<String>) preferences.get("hidden.pluginsEnabled");
        for (PluginLoader pluginLoader:plugins.values()) {
            var name = pluginLoader.getPluginInfo().getName();
            if (pluginLoader.isEnable()) {
                if (!pluginLoader.isEnabled()) {
                    try {
                        enablePlugin(pluginLoader);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!enabled.contains(name))enabled.add(name);
                }
            } else enabled.remove(name);
        }
    }

}

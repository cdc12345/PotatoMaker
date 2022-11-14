package org.cdc.potatomaker.plugin.loader;

import com.google.gson.Gson;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.exception.DefinedException;
import org.cdc.potatomaker.exception.PluginExistException;
import org.cdc.potatomaker.plugin.PluginInfo;
import org.cdc.potatomaker.plugin.PluginType;
import org.cdc.potatomaker.util.PluginLoaderBuilder;
import org.cdc.potatomaker.util.fold.RuntimeWorkSpaceManager;
import org.cdc.potatomaker.util.fold.UserFolderManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;

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

    private PMPluginLoader() {
        firstPluginPath = RuntimeWorkSpaceManager.getInstance().getPluginFolder();
        secondPluginPath = UserFolderManager.getInstance().getPluginsFolder();
        plugins = new HashMap<>();
    }

    public void loadPlugins() throws DefinedException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        listPlugins().forEach(a->{
            try {
                var loader = loadPlugin(a);
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
        });
        for (PluginLoader a:plugins.values()){
            PluginInfo info = a.getPluginInfo();
            //依赖处理
            if (info.getDepends()!=null&&info.getDepends().length!=0){
                for (String dep:info.getDepends()){
                    var dependLoader = plugins.get(dep).getClassLoader();
                    if (dependLoader == null) throw new DefinedException("无法找到插件"+info.getName()+"的依赖"+dep);
                    a.getClassLoader().addDependency(dependLoader);
                }
            }
            //插件实例处理,致敬Java服务器插件的模式
            if (info.getMain() != null) {
                try {
                    Class<?> mainClass = a.getClassLoader().loadClass(info.getMain());
                    AbstractPlugin mainIn = (AbstractPlugin) mainClass.getConstructor(new Class[0]).newInstance();
                    a.setPluginInstance(mainIn);
                    mainIn.inject(a);
                    if (Arrays.asList(info.getTypes()).contains(PluginType.VanillaAddon)) {
                        mainIn.onLoad();
                    }
                    //我觉得我在搞笑
                    mainIn.onEnable();
                } catch (ClassNotFoundException ignore) {
                    throw new DefinedException("无法载入插件" + info.getName() + "的主类" + info.getMain() + ",请开发者确定主类是否有误,开发者为"+Arrays.toString(info.getAuthors()));
                } catch (Exception e) {
                    throw e;
                }
            }
        }

    }

    public List<File> listPlugins(){
        FilenameFilter filenameFilter = (a,b)->"jar".
                equals(FilenameUtils.getExtension(b));
        ArrayList<File> plugins = new ArrayList<>(List.of(
                Objects.requireNonNull(firstPluginPath.listFiles(filenameFilter))));
        plugins.addAll(Arrays.asList(Objects.requireNonNull(secondPluginPath.listFiles(filenameFilter))));
        return plugins;
    }

    public PluginLoader loadPlugin(File plugin) throws MalformedURLException, PluginExistException {
        ModulePluginLoader modulePluginLoader = new ModulePluginLoader(plugin);
        return new PluginLoaderBuilder().setPluginPath(plugin).setLoaderType("java").setPluginClassLoader(modulePluginLoader)
                .setPluginInfo(loadPluginInfo(modulePluginLoader)).setLogger().build();
    }

    public PluginInfo loadPluginInfo(ModulePluginLoader pluginLoader){
        return new Gson().fromJson(new InputStreamReader(Objects.
                requireNonNull(pluginLoader.getResourceAsStream("plugin.json"))),PluginInfo.class);
    }

    public void unloadPlugins(){
        plugins.values().forEach(a->{
            if (a.getPluginInstance() != null)
                a.getPluginInstance().onDisable();
        }
        );
    }

}

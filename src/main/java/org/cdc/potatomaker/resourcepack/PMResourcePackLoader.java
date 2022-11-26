package org.cdc.potatomaker.resourcepack;

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.events.Events;
import org.cdc.potatomaker.events.ResourcePackEnabledEvent;
import org.cdc.potatomaker.events.ResourcePackLoadedEvent;
import org.cdc.potatomaker.util.PMVersion;
import org.cdc.potatomaker.util.fold.RuntimeWorkSpaceManager;
import org.cdc.potatomaker.util.resource.UIRE;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipFile;

import static org.cdc.potatomaker.util.ResourceManager.fromInputStream;

/**
 * e-mail: 3154934427@qq.com
 * 资源包载入器
 *
 * @author cdc123
 * @classname PMResourcePackLoader
 * @date 2022/11/15 9:28
 */
public class PMResourcePackLoader {
    public static PackLoader getPack(String name){
        return getInstance().packs.get(name);
    }

    public static void upPack(String name){
        getInstance().packList.remove(name);
        getInstance().packList.add(0,name);
    }

    private static final Logger LOGGER = LogManager.getLogger("PMResourcePack");

    private static PMResourcePackLoader instance;

    public static PMResourcePackLoader getInstance() {
        if (instance == null) {
            instance = new PMResourcePackLoader();
        }
        return instance;
    }



    private PMResourcePackLoader() {
        packs = new ConcurrentHashMap<>();
        invalidatePacks = new ConcurrentHashMap<>();
    }

    private final File resourcePack = RuntimeWorkSpaceManager.getInstance().getResourcePackFolder();

    private final ConcurrentHashMap<String,PackLoader> packs;

    private final ConcurrentHashMap<String,PackLoader> invalidatePacks;

    /**
     * 类似于mc的资源包,这个是资源包的排序,和mc一样.上面的会覆写下面的.
     */
    private final ArrayList<String> packList = new ArrayList<>();

    public List<File> listPacks(){
        final FilenameFilter filter = (a,b)-> "zip".equals(FilenameUtils.getExtension(b));
        var resourcePacks = resourcePack.listFiles(filter);
        if (resourcePacks == null) resourcePacks = new File[0];
        LOGGER.info("已经找到"+resourcePacks.length+"个资源包");
        return List.of(resourcePacks);
    }

    public PackInfo loadPackInfo(InputStream packInfo) throws IOException {
        return new Gson().fromJson(fromInputStream(packInfo),PackInfo.class);
    }

    public PackLoader loadPack(File resourcePack) throws IOException {
        ZipFile zipFile = new ZipFile(resourcePack);
        var packInfo = zipFile.getInputStream(zipFile.getEntry("pack.json"));
        InputStream packImage;
        try {
            packImage = zipFile.getInputStream(zipFile.getEntry("pack.png"));
        } catch (NullPointerException ignore){
            packImage = this.getClass().getResourceAsStream("/images/default-resource-icon.png");
        }
        return new PackLoader(loadPackInfo(packInfo),resourcePack,loadPackImage(packImage));
    }

    public Image loadPackImage(InputStream packImage) throws IOException {
        return ImageIO.read(packImage);
    }

    public boolean checkPack(PackInfo info){
        if (!info.getName().matches("[a-zA-Z_-]+")){
            return true;
        }
        return info.getMinVersion() > PMVersion.getInstance().getInnerVersion();
    }

    public void loadPacks(){
        for (File pack:listPacks()){
            //载入资源
            PackLoader loader;
            try {
                loader = loadPack(pack);
            } catch (IOException e){
                LOGGER.warn("无法载入资源包:"+pack+"可能是因为缺少pack.json");
                continue;
            } catch (NullPointerException e){
                LOGGER.warn("无法载入资源包:"+pack+",因为缺少pack.json");
                continue;
            }
            var info = loader.getInfo();
            if (checkPack(info)) {
                invalidatePacks.put(info.getName(), loader);
                continue;
            }
            packs.put(info.getName(),loader);
            if (info.getName().equals("pm-resources")){
                packList.add(info.getName());
            }
            LOGGER.info("成功载入资源包:"+info.getName()+",资源包目录:"+pack);
            LOGGER.info(info.getName()+":"+info.getDescription());

            Events.invokeEvent(new ResourcePackLoadedEvent(this, loader));
        }
    }

    public void enablePacks(){
        //清除所有已有的资源.方便重新启用,但说句实话,作用不大.
        UIRE.getInstance().clearResources();
        List<PackLoader> packs = packList.stream().map(PMResourcePackLoader::getPack).toList();

        for (PackLoader pack:packs){
            try {
                ZipFile zipFile = new ZipFile(pack.getPackFolder());
                zipFile.stream().forEach(entry -> {
                    if (entry.getName().matches("pack\\.json|pack\\.png")||entry.isDirectory()) return;
                    try {
                        UIRE.getInstance().addResource(entry.getName(), zipFile.getInputStream(entry));
                    } catch (IOException e) {
                        LOGGER.warn("无法载入资源包:"+pack.getInfo().getName());
                        e.printStackTrace();
                    }
                });
                Events.invokeEvent(new ResourcePackEnabledEvent(this,pack));
            } catch (IOException e){
                LOGGER.warn("无法载入资源包:"+pack.getInfo().getName()+"可能是因为此文件不存在");
                e.printStackTrace();
            }
        }
    }


}

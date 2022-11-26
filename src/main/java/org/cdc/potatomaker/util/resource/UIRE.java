package org.cdc.potatomaker.util.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.util.ResourceManager;
import org.cdc.potatomaker.util.fold.UserFolderManager;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * e-mail: 3154934427@qq.com
 * 仿照mcreator的资源管理类的管理类,盗版的UIRES(HAHAHAH
 *
 * @author cdc123
 * @classname UIRE
 * @date 2022/11/15 10:11
 */
public class UIRE {
    private static final Logger LOG = LogManager.getLogger("UIRE");

    private static UIRE instance;

    public static UIRE getInstance() {
        if (instance == null) {
            instance = new UIRE();
        }
        return instance;
    }
    private ConcurrentHashMap<String,byte[]> resources = new ConcurrentHashMap<>();
    private UIRE() {
        resources = new ConcurrentHashMap<>();
    }

    public void removeResource(String name) {resources.remove(name);}

    public void clearResources(){resources.clear();}

    public void addResource(String name,InputStream inputStream){
        try {
            resources.put(name,inputStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getResource(@NotNull Pattern name){
        return bytesToInput(resources.entrySet().stream().filter(a-> name.matcher(a.getKey()).find()).map(Map.Entry::getValue)
                .findFirst().orElse(new byte[0]));
    }

    public InputStream getResource(@NotNull String name){
        return getResource(Pattern.compile(identifierToResourcePath(name)));
    }

    public InputStream getResourceByKeyWord(@NotNull String name){
        return bytesToInput(resources.entrySet().stream().filter(a->a.getKey().contains(name)).map(Map.Entry::getValue)
                .findFirst().orElse(new byte[0]));
    }

    public java.util.List<InputStream> getResourcesByKeyWord(@NotNull String name){
        return resources.entrySet().stream().filter(a->a.getKey().contains(name)).map(a->bytesToInput(a.getValue())).toList();
    }

    /**
     * 此方法用来预防多次调用同一资源导致资源无法正常访问的问题.
     * @param bytes 输入的字节数组
     * @return 拷贝的输入流
     */
    private InputStream bytesToInput(byte[] bytes){
        if (bytes.length > 0) {
            return new ByteArrayInputStream(bytes);
        } else {
            return null;
        }
    }

    public File getResourceAsFile(@NotNull String name){
        var user = UserFolderManager.getInstance();
        var path = user.getCacheFolder().getPath()+"/"+name;
        user.writeResource(getResourceByKeyWord(name),path);
        return new File(path);
    }

    public URL getResourceAsURL(@NotNull String name){
        try {
            return getResourceAsFile(name).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ImageIcon get(String identifier) {
        if (!(identifier.endsWith(".png") || identifier.endsWith(".gif")))
            identifier += ".png";

        String textureIdentifier = "images." + identifier;


        // we start by checking if the loaded pack contains the image
        if (getResourceByKeyWord(identifierToResourcePath(textureIdentifier)) != null) {
            return getImageFromResourceID(textureIdentifier);
        } else { // if the loaded pack does not have the image, we fallback to the default one
            try {
                return new ImageIcon(ImageIO.read(
                        ResourceManager.getInnerResourceAsStream(identifierToResourcePath(textureIdentifier))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ImageIcon();
        }
    }

    public ImageIcon getImageFromResourceID(String identifier) {
        identifier = identifierToResourcePath(identifier);

        var resource = getResourceByKeyWord(identifier);
        if (resource != null) {
            try {
                var image = ImageIO.read(resource);
                if (image == null){
                    LOG.info("无法找到:"+identifier);
                    return new ImageIcon();
                }
                return new ImageIcon(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ImageIcon();
        }
        else {
            ImageIcon newItem = new ImageIcon(
                    Toolkit.getDefaultToolkit().createImage(getResourceAsURL(identifierToResourcePath(identifier))));
            addResource(identifierToResourcePath(identifier), getResource(Pattern.compile(identifierToResourcePath(identifier))));
            return newItem;
        }
    }

    public static String identifierToResourcePath(String identifier) {
        // parse identifier
        int lastDot = identifier.lastIndexOf('.');
        identifier = identifier.substring(0, lastDot).replace(".", "/") + identifier.substring(lastDot);

        return identifier;
    }
}

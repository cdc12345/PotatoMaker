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
import java.nio.charset.StandardCharsets;
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
    private ConcurrentHashMap<String,InputStream> resources = new ConcurrentHashMap<>();
    private UIRE() {
        resources = new ConcurrentHashMap<>();
    }

    public void removeResource(String name) {resources.remove(name);}

    public void clearResources(){resources.clear();}

    public void addResource(String name,InputStream inputStream){
        resources.put(name,inputStream);
    }

    public InputStream getResource(Pattern name){
        return resources.entrySet().stream().filter(a->name.matcher(a.getKey()).find()).map(Map.Entry::getValue)
                .findFirst().orElse(ResourceManager.getInnerResourceAsStream(name.pattern()));
    }

    public InputStream getResource(@NotNull String name){
        return resources.entrySet().stream().filter(a->name.equals(a.getKey())).map(Map.Entry::getValue)
                .findFirst().orElse(ResourceManager.getInnerResourceAsStream(name));
    }

    public File getResourceAsFile(@NotNull String name){
        var user = UserFolderManager.getInstance();
        var path = user.getCacheFolder().getPath()+"/"+name;
        user.writeResource(getResource(Pattern.compile(name)),path);
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

        String themedTextureIdentifier = "images." + identifier;

        // we start by checking if the loaded pack contains the image
        if (getResource(identifierToResourcePath(themedTextureIdentifier)) != null) {
            return getImageFromResourceID(themedTextureIdentifier);
        } else { // if the loaded pack does not have the image, we fallback to the default one
            return getImageFromResourceID("themes.default_dark.images." + identifier);
        }
    }

    public ImageIcon getImageFromResourceID(String identifier) {
        identifier = identifierToResourcePath(identifier);

        if (getResource(identifierToResourcePath(identifier)) != null) {
            try {
                return new ImageIcon(ImageIO.read(getResource(Pattern.compile(identifierToResourcePath(identifier)))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            ImageIcon newItem = new ImageIcon(
                    Toolkit.getDefaultToolkit().createImage(getResourceAsURL(identifierToResourcePath(identifier))));
            resources.put(identifierToResourcePath(identifier), getResource(Pattern.compile(identifierToResourcePath(identifier))));
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
